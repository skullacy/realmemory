package com.example.realmemory;

import java.io.UnsupportedEncodingException;

import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

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
        
        SlidingMenu menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.10f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        menu.setMenu(R.layout.grouplist);
        

        Intent intent = getIntent();
        setMember_srl(intent.getExtras().get("member_srl").toString());
        setNick_name(intent.getExtras().get("nick_name").toString());
        
        //skullacy added
        //사진 리스트 불러오기
        Thread thread = new Thread(new Runnable(){
        	public void run(){
        		CommServerJson comm = new CommServerJson();
                comm.setParam("module", "realmemory");
                comm.setParam("act", "dispRealmemoryGetGalleryList");
                comm.setParam("member_srl", getMember_srl());
                String result = null;
                try {
        			result = comm.getData();
        		} catch (UnsupportedEncodingException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
                Log.e("getGalleryList", result);
        	}
        });
        thread.start();
        
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
			intent.putExtra("member_srl", getMember_srl());
			intent.putExtra("nick_name", getNick_name());
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
