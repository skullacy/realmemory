package com.example.realmemory;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.example.realmemory.util.CommServerJson;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
	
	static String nick_name;
	
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
				nick_name = nick.getText().toString();
				dialog = new ProgressDialog(RegnickActivity.this);
				dialog.setTitle("");
				dialog.setMessage("서버에 등록중입니다.");
				dialog.show();
				alert = new AlertHandler();
				regThread();
			}
		});
	}
	
	public void regThread(){
		new Thread(new Runnable(){
			public void run(){
				try{
					//서버와 통신
					CommServerJson comm = new CommServerJson();
					comm.setParam("module", "realmemory");
					comm.setParam("act", "procRealmemoryInsertNickname");
					comm.setParam("phone", phoneNum.toString());
					comm.setParam("nick_name", nick_name);
					JSONObject jsonobj = new JSONObject();
					jsonobj = comm.getJSONData();
					dialog.dismiss();
					
					//전송받은 결과 파싱후 처리
					if(jsonobj.getInt("error") == 0)
					{
						Log.e("insert Account", "success regist");
						Intent i = new Intent(RegnickActivity.this, MainActivity.class);
						i.putExtra("member_srl", jsonobj.getString("member_srl"))
						 .putExtra("nick_name", jsonobj.getString("nick_name"));
						
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


