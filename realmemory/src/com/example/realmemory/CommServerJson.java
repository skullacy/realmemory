package com.example.realmemory;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class CommServerJson {
	private static String serverUrl;
	public static String getServerUrl() {
		return serverUrl;
	}
	public static void setServerUrl(String serverUrl) {
		CommServerJson.serverUrl = serverUrl;
	}
	
	
	private static List<NameValuePair> params;
	public void setParam(String query, String value){
		params.add(new BasicNameValuePair(query, value));
	}
	public String getServerUrlWithQueryString(){
		return null;
	}
	
	
	
	CommServerJson(){
		if(getServerUrl() == null){
			setServerUrl("http://skullacytest.cafe24.com");
		}
	}
	
	

	
	
	
	
	
}
