package com.example.realmemory;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;



public class AddImageActivity extends Activity {
	
	private static final String TEMP_PHOTO_FILE = "temp.jpg";
	private static final int REQ_CODE_PICK_IMAGE = 0;
	private Button      PickDate;
	private int         mYear;    
	private int         mMonth;    
	private int         mDay;    
	static final int DATE_DIALOG_ID = 0;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_addimage);
	    Intent intent = new Intent(
				Intent.ACTION_GET_CONTENT,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		intent.setType("image/*");
		intent.putExtra("crop", "true");  
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
		intent.putExtra("outputFormat",
				Bitmap.CompressFormat.JPEG.toString());
		startActivityForResult(intent, REQ_CODE_PICK_IMAGE);
		
		//��¥ ���̾�α�
	    PickDate = (Button)findViewById(R.id.dateb); 
	    PickDate.setOnClickListener(new View.OnClickListener()
	    {                         
	         
			public void onClick(View v) {                
				showDialog(DATE_DIALOG_ID);
				
				}        
			});
	   
		    final Calendar c = Calendar.getInstance();                 
			mYear = c.get(Calendar.YEAR);        
			mMonth = c.get(Calendar.MONTH);        
			mDay = c.get(Calendar.DAY_OF_MONTH);                 
			updateDisplay();    
		}        
	
		private void updateDisplay()   
		{
			PickDate.setText(String.format("%04d�� %02d�� %02d��", mYear, mMonth+1, mDay));    
		}
	
		private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener()
		{                                 
		            
			public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {                    
				//                     
				mYear = year;                    
				mMonth = monthOfYear;                    
				mDay = dayOfMonth;                    
				updateDisplay();                
				}
          
			};     
		@Override    
		protected Dialog onCreateDialog(int id) 
		{        
			switch(id)
			{        
			case DATE_DIALOG_ID:            
				return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);        
			}        
			return null;    
		}

	    // TODO Auto-generated method stub
	
	 
	
	
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
		    	{
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
	
}
