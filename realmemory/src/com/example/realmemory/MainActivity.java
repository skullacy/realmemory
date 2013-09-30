package com.example.realmemory;

import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
	
	//member_srl 설정
	private static String member_srl;
	public static String getMember_srl() {
		return member_srl;
	}
	public static void setMember_srl(String member_srl) {
		MainActivity.member_srl = member_srl;
	}
	
	//nick_name 설정
	private static String nick_name;
	public static String getNick_name() {
		return nick_name;
	}
	public static void setNick_name(String nick_name) {
		MainActivity.nick_name = nick_name;
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Intent intent = getIntent();
        setMember_srl(intent.getExtras().get("member_srl").toString());
        setNick_name(intent.getExtras().get("nick_name").toString());
        
    }
    
    

    @TargetApi(Build.VERSION_CODES.HONEYCOMB) @SuppressLint("NewApi") @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mainbar, menu);
        ActionBar actionBar = getActionBar();
        getActionBar().setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
		
		switch(item.getItemId()){
		case R.id.item1:
			Intent intent = new Intent(this, AddImageActivity.class);
			startActivity(intent);
			
			break;
			
		case R.id.item2:
			break;
		
		case R.id.item3:
			break;
			
		default:
			return false; 
		}
    	
    	return true;
    }
	



	
   
   
    
    
}
