package com.qdedu.base_module_web.init.jsbridge;


public interface WebViewJavascriptBridge {


	public void send(String data);
	public void send(String data, CallBackFunction responseCallback);
	
	

}
