package com.sbolo.syk.common.http.interceptor.inner;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class SafetyCheckIntercepter implements Interceptor {
	
	private static final Logger log = LoggerFactory.getLogger(SafetyCheckIntercepter.class);
	private int maxRetry = 2;

	@Override
	public Response intercept(Chain chain) throws IOException {
		Request request = chain.request();
		String oldUrl = request.url().toString();
		Response response = chain.proceed(request);
		if(response.code() != 503){
			return response;
		}
		for(int retryNum=1;retryNum<=maxRetry;retryNum++){
			try {
				byte[] bytes = response.body().bytes();
				String content = new String(bytes, "utf-8");
				Matcher m = Pattern.compile("(\\w+)=\\{\"(\\w+)\":([\\+\\(\\!\\[\\]\\)]+)\\};").matcher(content);
				if(!m.find()){
					log.info("The response code is \"503\", but it's not \"Browser Safety Check\". from page {}.", oldUrl);
					break;
				}
				String de1 = m.group(1);
				String de2 = m.group(2);
				String answer = m.group(3);
			
				String checkPageStr = checkPageUrl(request, de1, de2, answer, content);
				request = request.newBuilder().url(checkPageStr).build();
				Thread.sleep(6000);
				if(retryNum == 0){
					log.info("Start to \"Browser Safety Check\" with page {}, for page {}", checkPageStr, oldUrl);
				}
				
				response = chain.proceed(request);
				if(response.code() == 503){
					if(retryNum == maxRetry){
						log.error("Browser Safety Check retry over!!! still code 503");
						break;
					}
					log.error("Browser Safety Check still 503, {}/{}", retryNum+1, maxRetry);
					continue;
				}
				if(response.isSuccessful() && !oldUrl.equals(response.request().url().toString())){
					request = request.newBuilder().url(oldUrl).build();
					response = chain.proceed(request);
				}
				break;
			} catch (Exception e) {
				if(e instanceof ScriptException || e instanceof ScriptException){
					if(retryNum == maxRetry){
						log.error("Browser Safety Check retry over!!! Exception: {}", e.getMessage());
						break;
					}
					log.error("Browser Safety Check retry {}/{}, Exception: {}", retryNum+1, maxRetry, e.getMessage());
				}
			}
		}
		
		return response;
	}
	
	private String checkPageUrl(Request request, String de1, String de2, String answer, String content) throws ScriptException{
		String action = "/cdn-cgi/l/chk_jschl";
		String host = request.url().host();
		StringBuffer jsSyntax = new StringBuffer();
		jsSyntax.append("var jschl_answer=").append(answer).append(";");
		Matcher m2 = Pattern.compile(de1+"\\."+de2+"([-+*]=[\\+\\(\\!\\[\\]\\)]+;)").matcher(content);
		while(m2.find()){
			jsSyntax.append("jschl_answer").append(m2.group(1));
		}
		jsSyntax.append("jschl_answer = parseInt(jschl_answer, 10) + "+host.length()+";");
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("javascript");
		int jschl_answer = ((Double) engine.eval(jsSyntax.toString())).intValue();
		m2 = Pattern.compile("name=\"pass\" value=\"(.*?)\"/>").matcher(content);
		m2.find();
		String pass = m2.group(1);
		m2 = Pattern.compile("name=\"jschl_vc\" value=\"(\\w+)\"/>").matcher(content);
		m2.find();
		String jschl_vc = m2.group(1);
		String protocol = request.isHttps()? "https://" : "http://";
		StringBuffer checkPage = new StringBuffer();
		checkPage.append(protocol).append(host).append(action).append("?jschl_vc=")
		.append(jschl_vc).append("&pass=").append(pass).append("&jschl_answer=").append(jschl_answer);
		return checkPage.toString();
	}

}
