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
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.*;

public class IntroActivity extends Activity {
	
	Handler h;
	private static String phoneNum;
	static TextView loadingMsg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
		
		loadingMsg = (TextView) findViewById(R.id.loading_msg);
		
		//현재 핸드폰의 전화번호 가져오기
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		phoneNum = tm.getLine1Number();
		
		//어드민 테스트용 admin_nosignup : 디버그모드(등록안됨), admin_signup : 디버그모드(회원가입)
		//phoneNum = "admin_nosignup";
		//phoneNum = "admin_signup";
		phoneNum = "01083696664";
		
		h = new Handler();
		h.postDelayed(irun, 1000);
		
	}
	
	Runnable irun = new Runnable() {
		public void run() {
			IntroActivity.loadingMsg.setText("서버와 통신하고 있습니다.");
			
			//전송할 파라미터 생성
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("module", "realmemory"));
			params.add(new BasicNameValuePair("phone", phoneNum.toString()));
			
			//json 스레드 실행(서버접속 후 계정확인)
			new JsonLoadingTask().execute(params);
		}
	};
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		h.removeCallbacks(irun);
	}
	
	public String getJsonText(List<NameValuePair>[] params){
		try{
			String line = getStringFromUrl("http://skullacytest.cafe24.com/", params);
			Log.e("getJsonText", line);
			return line;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public String getStringFromUrl(String url, List<NameValuePair>[] params) throws UnsupportedEncodingException {
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
	
	public static InputStream getInputStreamFromUrl(String url, List<NameValuePair>[] params){
		InputStream contentStream = null;
		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params[0], HTTP.UTF_8);
			post.setEntity(ent);
			HttpResponse responsePost = httpclient.execute(post);
			
			contentStream = responsePost.getEntity().getContent();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return contentStream;
	}
	
	private class JsonLoadingTask extends AsyncTask<List<NameValuePair>, Void, String> {
		protected String doInBackground(List<NameValuePair>... params) {
			return getJsonText(params);
		}
		
		protected void onPostExecute(String result) {
			try{
				JSONObject jsonobj = new JSONObject(result);
				String next_act = jsonobj.getString("nextActivity");
				
				//닉네임 생성 액티비티로 이동
				if(next_act.equals("regnickname")){
					Intent i = new Intent(IntroActivity.this, RegnickActivity.class);
					i.putExtra("phoneNum", IntroActivity.phoneNum.toString());
					startActivity(i);
					finish();
					overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				}
				//사진보기 액티비티로 이동
				else if(next_act.equals("photolist")){
					Intent i = new Intent(IntroActivity.this, MainActivity.class);
					i.putExtra("phoneNum", jsonobj.getString("phone"))
						.putExtra("member_srl", jsonobj.getString("member_srl"))
						.putExtra("nick_name", jsonobj.getString("nick_name"));
					startActivity(i);
					finish();
					overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				}
					
				
			}
			catch(JSONException e)
			{
				e.printStackTrace();
			}
			
			
		}
	}
	

}
