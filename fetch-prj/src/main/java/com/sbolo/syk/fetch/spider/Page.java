package com.sbolo.syk.fetch.spider;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Page {
	private Document document;
	private List<String> newUrls = new ArrayList<String>();
	private String url;
	private String host;
	private String charset;
	 
	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public void setNewUrls(List<String> newUrls) {
		this.newUrls = newUrls;
	}

	public String getHost() {
		return host;
	}


	public void setHost(String host) {
		this.host = host;
	}


	public Page(Document document){
		this.document = document;
	}
	

	public List<String> getNewUrls() {
		return newUrls;
	}


	public void addNewUrls(List<String> newUrls) {
		this.newUrls.addAll(newUrls);
	}
	
	public void addNewUrl(String url){
		this.newUrls.add(url);
	}

	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	
	public List<String> getAndWipeUrls() {
		List<String> urls = this.getNewUrls();
		return urls;
	}
	
	public List<String> links(Elements es, String attrName){
		List<String> l = new ArrayList<String>();
		int size = es.size();
		for(int i=0; i<size; i++){
			Element selectElement = es.get(i);
			String url = link(selectElement, attrName);
			l.add(url);
		}
		return l;
	}
	
	public List<String> links(Elements es, String attrName, int count){
		List<String> l = new ArrayList<String>();
		int size = es.size();
		for(int i=0; i<size; i++){
			Element selectElement = es.get(i);
			String url = link(selectElement, attrName);
			l.add(url);
			if(i+1 == count) {
				break;
			}
		}
		return l;
	}
	
	public String link(Element selectElement, String attrName){
		String url = selectElement.attr(attrName);
		if(!url.startsWith("http://") && !url.startsWith("https://")){
			if(url.startsWith("/")){
				url = this.getHost()+url;
			}else {
				url = this.getHost()+"/"+url;
			}
		}
		return url;
	}
}
