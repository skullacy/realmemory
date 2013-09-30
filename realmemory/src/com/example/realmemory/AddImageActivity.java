package com.example.realmemory;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.realmemory.RegnickActivity.AlertHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;



public class AddImageActivity extends Activity {
	
	private static final String TEMP_PHOTO_FILE = "temp.jpg";
	private static final int REQ_CODE_PICK_IMAGE = 0;
	Bitmap selectedImage;
	private Button      PickDate;
	private int         mYear;    
	private int         mMonth;    
	private int         mDay;    
	static final int DATE_DIALOG_ID = 0;
	
	
	/**
	 * skullacy added
	 * ����Ʈ���� �г���, member_srl�޾ƿ���.
	 */
	private static String nick_name;
	public static String getNick_name() {
		return nick_name;
	}
	public static void setNick_name(String nick_name) {
		AddImageActivity.nick_name = nick_name;
	}
	
	private static String member_srl;
	public static String getMember_srl() {
		return member_srl;
	}
	public static void setMember_srl(String member_srl) {
		AddImageActivity.member_srl = member_srl;
	}
	
	/**
	 * skullacy added
	 * ��� ���μ����� ��ü ����
	 */
	ProgressDialog commDialog;
	AlertDialog.Builder commAlertDialog;
	AlertHandler commAlert; 
	
	public static final int SEND_ALERT = 0;
	public static final int FINISH_ACTIVITY = 1;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_addimage);
	    
	    //skullacy added
	    //main���� member_srl, nickname �޾ƿ���
	    Intent getIntent = getIntent();
	    setMember_srl(getIntent.getExtras().get("member_srl").toString());
	    setNick_name(getIntent.getExtras().get("nick_name").toString());
	    
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
		
		//skullacy added
		//Submit��ư Ŭ���̺�Ʈ
		Button submitBtn = (Button) findViewById(R.id.addimageSubmitBtn);
		submitBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//�ǵ��� ��ü ���� dialog, alert
				commDialog = new ProgressDialog(AddImageActivity.this);
				commDialog.setTitle("");
				commDialog.setMessage("������ ������Դϴ�.");
				commDialog.show();
				commAlert = new AlertHandler();
				
				Thread thread = new Thread(new Runnable(){
					public void run(){
						EditText titleobj = (EditText) findViewById(R.id.subject);
						EditText positionobj = (EditText) findViewById(R.id.position);
						EditText descobj = (EditText) findViewById(R.id.description);
						
						//������ſ� ��ü ����
						CommServerJson comm = new CommServerJson();
						comm.setParam("module", "realmemory");
						comm.setParam("act", "procRealmemoryUploadImage");
						comm.setParam("member_srl", getMember_srl());
						comm.setParam("title", titleobj.getText().toString());
						comm.setParam("position", positionobj.getText().toString());
						comm.setParam("description", descobj.getText().toString());
						comm.setParam("picdate", String.format("%04d%02d%02d000000", mYear, mMonth+1, mDay));
						comm.insertImage(selectedImage);
						
						//��������� json��ü �޾ƿ���
						JSONObject jsonobj = new JSONObject();
						try {
							jsonobj = comm.getJSONData();
							commDialog.dismiss();
							
							//���۹��� ��� �Ľ��� ó��
							if(jsonobj.getInt("error") == 0){
								finish();
							}
							else{
								commAlert.sendMessage(commAlert.obtainMessage(SEND_ALERT, jsonobj.getString("message")));
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						//Ȯ�ο� �α�
						Log.e("CommServer in submitBtnEvent", jsonobj.toString());
					}
				});
				thread.start();
			}
		});
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
    //��Ƽ��Ƽ�� �����Ͽ����� �̹��� ���ä�������
    protected void onActivityResult(int requestCode, int resultCode, Intent imageData){
    	super.onActivityResult(requestCode, resultCode, imageData);
    	
    	switch(requestCode){
	    	case REQ_CODE_PICK_IMAGE:
	    	{
	    		if(resultCode == RESULT_OK)
	    		{
	    			if(imageData != null)
	    			{
	    				String filePath = Environment.getExternalStorageDirectory() + "/temp.jpg";
	    				//�α�Ĺ���� ��� Ȯ��
	    				System.out.println("path" + filePath);
	    				//temp.jpg������ Bitmap���� ���ڵ�/////
	    				selectedImage = BitmapFactory.decodeFile(filePath);
	    				ImageView _image = (ImageView)findViewById(R.id.imageView);
	    				_image.setImageBitmap(selectedImage);
	    			}
	    		}
	    	break;
	    	}
    	
    	}
    	
    }

    public class AlertHandler extends Handler{
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case SEND_ALERT:
				Log.e("alert Handler", (String)msg.obj);
				commAlertDialog = new AlertDialog.Builder(AddImageActivity.this);
				commAlertDialog.setTitle("").setMessage((String)msg.obj).setNeutralButton("Ȯ��", null).show();
			}
		}
	}
	
}
