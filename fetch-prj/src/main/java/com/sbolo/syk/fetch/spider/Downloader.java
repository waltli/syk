package com.sbolo.syk.fetch.spider;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Response;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sbolo.syk.common.exception.DownloadException;
import com.sbolo.syk.common.http.HttpUtils;
import com.sbolo.syk.common.http.HttpUtils.HttpResult;
import com.sbolo.syk.common.http.callback.HttpSendCallback;
import com.sbolo.syk.common.http.callback.HttpSendCallbackPure;
import com.sbolo.syk.common.tools.Utils;

@Component
public class Downloader {
	
	private static final Logger log = LoggerFactory.getLogger(Downloader.class);
	
	public static void main(String[] args) throws Exception {
		HttpUtils.httpGet("https://movie.douban.com/subject/26387939", new HttpSendCallbackPure() {
			
			@Override
			public void onResponse(Response response) throws Exception {
				// TODO Auto-generated method stub
			}
		});
		
	}
	
	public Page download(final String url) throws DownloadException{
		HttpResult<Page> result = HttpUtils.httpGet(url, new HttpSendCallback<Page>() {
			@Override
			public Page onResponse(Response response) throws Exception {
				if(!response.isSuccessful()){
					throw new DownloadException("Download the page was faild, code: "+response.code());
				}
				byte[] bytes = response.body().bytes();
				String charset = getHtmlCharset(response, bytes);
				String content = new String(bytes, charset);
				Page page = new Page(Jsoup.parse(content));
				page.setUrl(url);
				page.setHost(getHost(url));
				page.setCharset(charset);
				return page;
			}
		});
		try {
			return result.getValue();
		} catch (Exception e) {
			if (e instanceof DownloadException) {
				throw (DownloadException) e;
			}
			throw new DownloadException(e);
		}
	}
	
	private String getHost(String url){
        if(url==null||url.trim().equals("")){
            return "";
        }
        String host = "";
        Pattern p =  Pattern.compile("(http://|)((\\w)+\\.)+\\w+");
        Matcher matcher = p.matcher(url);  
        if(matcher.find()){
            host = matcher.group();  
        }
        return host;
    }
	
	private String getHtmlCharset(Response response, byte[] bytes) throws IOException {
        String charset;
        // charset
        // 1、encoding in http header Content-Type
        String value = response.header("Content-Type");
        charset = Utils.getCharset(value);
        if (StringUtils.isNotBlank(charset)) {
            log.debug("Auto get charset: {}", charset);
            return charset;
        }
        // use default charset to decode first time
        String defaultCharset = "utf-8";
        String content = new String(bytes, defaultCharset);
        // 2、charset in meta
        Document document = Jsoup.parse(content);
        Elements links = document.select("meta");
        for (Element link : links) {
            // 2.1、html4.01 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
            String metaContent = link.attr("content");
            String metaCharset = link.attr("charset");
            if (metaContent.indexOf("charset") != -1) {
                metaContent = metaContent.substring(metaContent.indexOf("charset"), metaContent.length());
                charset = metaContent.split("=")[1];
                break;
            }
            // 2.2、html5 <meta charset="UTF-8" />
            else if (StringUtils.isNotEmpty(metaCharset)) {
                charset = metaCharset;
                break;
            }
        }
        if(StringUtils.isBlank(charset)){
        	charset = defaultCharset;
        }
        
        log.debug("Auto get charset: {}", charset);
        // 3、todo use tools as cpdetector for content decode
        return charset;
    }
	
	
}
