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
	
	private static final String TEMP_PHOTO_FILE = "temp.jpg";
	private static final int REQ_CODE_PICK_IMAGE = 0;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       
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
			
			Intent intent = new Intent(
					Intent.ACTION_GET_CONTENT,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			intent.setType("image/*");
			intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
			intent.putExtra("outputFormat",
					Bitmap.CompressFormat.JPEG.toString());
			startActivityForResult(intent, REQ_CODE_PICK_IMAGE);
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
    	
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    	return true;
    	
    }
    //�ӽ� ���� ������ ��θ� ��ȯ
    private Uri getTempUri(){
    	return Uri.fromFile(getTempFile());
    }
    //����޸𸮿� �ӽ� �̹��� ������ �����Ͽ� �� ������ ��θ� ��ȯ
    private File getTempFile(){
    	if(isSDCARDMOUNTED()){
    		File f = new File(Environment.getExternalStorageDirectory(),
    				TEMP_PHOTO_FILE);
    		try{
    			f.createNewFile();
    		}catch (IOException e){
    			
    		}
    		return f;
    	}else
    		return null;
    }
    //SDī�尡 ����Ʈ �Ǿ� �ִ��� Ȯ��
    private boolean isSDCARDMOUNTED(){
    	String status = Environment.getExternalStorageState();
    	if(status.equals(Environment.MEDIA_MOUNTED))
    		return true;
    	
    	return false;
    }
    //��Ƽ��Ƽ�� �����Ͽ����� �̹��� ����
    protected void onActivityResult(int requestCode, int resultCode, Intent imageData){
    	super.onActivityResult(requestCode, resultCode, imageData);
    	
    	switch(requestCode){
    	case REQ_CODE_PICK_IMAGE:
    		if(resultCode == RESULT_OK){
    			if(imageData != null){
    				String filePath = Environment.getExternalStorageDirectory() + "/temp.jpg";
    				//�α�Ĺ���� ��� Ȯ��
    				System.out.println("path" + filePath);
    				//temp.jpg������ Bitmap���� ���ڵ�
    				Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
    				ImageView _image = (ImageView)findViewById(R.id.imageView);
    				_image.setImageBitmap(selectedImage);
    			}
    		}
    	break;
    	}
    }
    
    
}
