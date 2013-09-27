package com.example.realmemory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegnickActivity extends Activity {
	
	private static String phoneNum;
	public static String getPhoneNum() {
		return phoneNum;
	}

	public static void setPhoneNum(String phoneNum) {
		RegnickActivity.phoneNum = phoneNum;
	}
	
	static String nickname;
	
	ProgressDialog dialog;
	AlertDialog.Builder alertDialog;
	AlertHandler alert;
	
	public static final int SEND_ALERT = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_regnick);
	    
	    Intent intent = getIntent();
	    setPhoneNum(intent.getExtras().get("phoneNum").toString());
	    
	    Log.e("RegnickActivity", getPhoneNum());
	    
	    Button submitBtn = (Button) findViewById(R.id.regnick_submit);
	    submitBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText nick = (EditText) findViewById(R.id.nickname);
				nickname = nick.getText().toString();
				regThread();
			}
		});
	}
	
	public void regThread(){
		dialog = new ProgressDialog(this);
		dialog.setTitle("");
		dialog.setMessage("서버에 등록중입니다.");
		dialog.show();
		alert = new AlertHandler();
		new Thread(new Runnable(){
			public void run(){
				try{
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("module", "realmemory"));
					params.add(new BasicNameValuePair("act", "procRealmemoryInsertNickname"));
					params.add(new BasicNameValuePair("phone", phoneNum.toString()));
					params.add(new BasicNameValuePair("nickname", nickname));
					
					//서버에 계정 등록
					String line = getStringFromUrl("http://skullacytest.cafe24.com/", params);
					Log.e("insert Account", line);
					dialog.dismiss();
					
					//전송받은 결과 파싱후 처리
					JSONObject jsonobj = new JSONObject(line);
					if(jsonobj.getInt("error") == 0)
					{
						Log.e("insert Account", "success regist");
						Intent i = new Intent(RegnickActivity.this, MainActivity.class);
						
						startActivity(i);
						finish();
						overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
					}
					else
					{
						alert.sendMessage(alert.obtainMessage(SEND_ALERT, jsonobj.getString("message")));
					}
				}
				catch(Throwable e){
					e.printStackTrace();
				}
				
				
			}
		}).start();
		
		
	}
	
	public String getStringFromUrl(String url, List<NameValuePair> params) throws UnsupportedEncodingException {
		BufferedReader br = new BufferedReader(new InputStreamReader(getInputStreamFromUrl(url, params), "utf-8"));
		StringBuffer sb = new StringBuffer();
		
		try{
			String line = null;
			
			while((line = br.readLine()) != null){
				sb.append(line);
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	
	public static InputStream getInputStreamFromUrl(String url, List<NameValuePair> params){
		InputStream contentStream = null;
		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
			post.setEntity(ent);
			HttpResponse responsePost = httpclient.execute(post);
			
			contentStream = responsePost.getEntity().getContent();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return contentStream;
	}
	
	public class AlertHandler extends Handler{
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case SEND_ALERT:
				Log.e("alert Handler", (String)msg.obj);
				alertDialog = new AlertDialog.Builder(RegnickActivity.this);
				alertDialog.setTitle("").setMessage((String)msg.obj).setNeutralButton("확인", null).show();
			}
		}
	}

	

}


