package com.example.realmemory;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
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
			
			//json 스레드 실행(서버접속 후 계정확인)
			new JsonLoadingTask().execute();
		}
	};
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		h.removeCallbacks(irun);
	}
	
	private class JsonLoadingTask extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... str) {
			CommServerJson comm = new CommServerJson();
			comm.setParam("module", "realmemory");
			comm.setParam("phone", phoneNum.toString());
			String data = null;
			try {
				data = comm.getData();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return data;
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
