package com.sbolo.syk.common.http.proxy;

import java.net.InetSocketAddress;
import java.net.Proxy;

public class StatefulProxy {
	private String host;
	private Integer port;
	private Proxy proxy;
	
	public StatefulProxy(String host, Integer port){
		this.host = host;
		this.port = port;
		this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
	}
	
	public boolean equals(StatefulProxy obj) {
		if(host.equals(obj.host()) && port == obj.port()){
			return true;
		}
		return false;
	}

	public Proxy proxy(){
		return proxy;
	}
	
	public String host() {
		return host;
	}
	
	public Integer port(){
		return port;
	}
	
}
