package com.example.realmemory;

import java.io.File;
import java.io.IOException;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       //asklgjaslk;dfh;aklsgjaskldfhasklg;asjdflkasjfdl
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
		String text =null;
		
		switch(item.getItemId()){
		case R.id.item1:
			Intent intent = new Intent(this, AddImageActivity.class);
			startActivity(intent);
			
			break;
			
		case R.id.item2:
			text = "Action item, icon only, always displayed";
			break;
		
		case R.id.item3:
			text = "Normal menu item";
			break;
			
		default:
			return false;
		}
    	
    	return true;
    	
    }
   
   
    
    
}
