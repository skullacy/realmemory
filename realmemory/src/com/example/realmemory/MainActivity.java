package com.example.realmemory;

import java.io.UnsupportedEncodingException;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.example.realmemory.util.CommServerJson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {
	
	//member_srl ����
	private static String member_srl; 
	public static String getMember_srl() {
		return member_srl;
	}
	public static void setMember_srl(String member_srl) {
		MainActivity.member_srl = member_srl;
	}
	
	//nick_name ����
	private static String nick_name;
	public static String getNick_name() {
		return nick_name;
	}
	public static void setNick_name(String nick_name) {
		MainActivity.nick_name = nick_name;
	}
	
	protected ListFragment mList;
	private Fragment mContent;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 		
 		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
		if (mContent == null)
			mContent = new ContentFragment("list");	
		
		// ���� fragment ����
		setContentView(R.layout.fragment_content);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, mContent)
		.commit();
		
		// ���� fragment ����(�����̵�޴� - ����)
		setBehindContentView(R.layout.fragment_leftmenu);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame, new LeftSlideListFragment())
		.commit();
		
		SlidingMenu sm = getSlidingMenu();
 		sm.setShadowWidthRes(R.dimen.shadow_width);
 		sm.setShadowDrawable(R.drawable.shadow);
 		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
 		sm.setFadeDegree(0.35f);
 		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
 		

        Intent intent = getIntent();
        setMember_srl(intent.getExtras().get("member_srl").toString());
        setNick_name(intent.getExtras().get("nick_name").toString());
        
        //skullacy added
        //���� ����Ʈ �ҷ�����
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
    
    @Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	getSupportMenuInflater().inflate(R.menu.mainbar, menu);
        ActionBar actionBar = getSupportActionBar();
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
    	
		return super.onOptionsItemSelected(item);
    }
	



	
   
   
    
    
}
