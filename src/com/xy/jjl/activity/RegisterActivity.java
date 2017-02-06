package com.xy.jjl.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.xy.jjl.R;
import com.xy.jjl.R.anim;
import com.xy.jjl.R.id;
import com.xy.jjl.R.layout;
import com.xy.jjl.application.ApplicationInfo;
import com.xy.jjl.common.APPConfig;
import com.xy.jjl.image_activity.ImageShowActivity;
import com.xy.jjl.utils.OkHttpUtils;
import com.xy.jjl.utils.OkHttpUtils.Param;
import com.xy.jjl.utils.OkHttpUtils.ResultCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import io.vov.vitamio.utils.Log;

public class RegisterActivity extends Activity implements OnClickListener {
	
	private ImageView registerBack;
	private EditText register_username;
	private EditText register_pwd;
	private EditText register_pwd_confirm;
	private EditText register_phone;
	private EditText register_id;
	private EditText register_region;
	private EditText register_address;
	private EditText register_device_no;
	private Button register_btn;
	
	private String username;
	private String pwd;
	private String pwd_confirm;
	private String phone;
	private String id;
	private String region;
	private String address;
	private String deviceno;
	private String addTime;
	private String logintime;
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private String flag;
	private ProgressDialog progressDialog=null;
	
	private Handler myHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			StringBuffer sb;
			Dialog mdialog;
			
			switch (msg.what) {
			case 0:
				progressDialog.dismiss();
				sb = new StringBuffer();
				sb.append("注册失败!");
				mdialog = new AlertDialog.Builder(RegisterActivity.this).setTitle("提示")
						.setMessage(sb.toString())// 提示内容
						.setPositiveButton("确定", null)// 设置确定按钮
						.create();// 创建
				mdialog.show();
				break;
			case 1:
				progressDialog.dismiss();
				sb = new StringBuffer();
				sb.append("此店名已被注册!");
				mdialog = new AlertDialog.Builder(RegisterActivity.this).setTitle("提示")
						.setMessage(sb.toString())// 提示内容
						.setPositiveButton("确定", null)// 设置确定按钮
						.create();// 创建
				mdialog.show();
				break;
				
			case 2:
				progressDialog.dismiss();
				sb = new StringBuffer();
				sb.append("注册成功!");
				mdialog = new AlertDialog.Builder(RegisterActivity.this).setTitle("提示")
						.setMessage(sb.toString())// 提示内容
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								GotoMainAcivity();
							}
						})// 设置确定按钮
						.create();// 创建
				
				mdialog.show();
				
				break;
			default:
				break;
			}
		}
	};
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_register);
		ApplicationInfo.getInstance().addActivity(this);
		
		init();
		
		//Test 
		initTestData();
	}
	
	private void init(){
		
		registerBack=(ImageView)findViewById(R.id.registerBack);
		register_username=(EditText)findViewById(R.id.et_register_shop_name);
		register_pwd=(EditText)findViewById(R.id.et_register_pwd);
		register_pwd_confirm=(EditText)findViewById(R.id.et_register_pwd_confirm);
		register_phone=(EditText)findViewById(R.id.et_register_phone);
		register_id=(EditText)findViewById(R.id.et_register_ID);
		register_region=(EditText)findViewById(R.id.et_register_region);
		register_address=(EditText)findViewById(R.id.et_register_address);
		register_device_no=(EditText)findViewById(R.id.et_register_device_no);
		register_btn=(Button)findViewById(R.id.register_btn);
 
		registerBack.setOnClickListener(this);
		register_btn.setOnClickListener(this);
		
		progressDialog=new ProgressDialog(this);
	}
	
	private void initTestData(){
		register_username.setText("panyudian");
		register_pwd.setText("aaa");
		register_pwd_confirm.setText("aaa");
		register_phone.setText("12345678901");
		register_id.setText("123456789012345678");
		register_region.setText("番禺区");
		register_address.setText("番禺区市桥");
		register_device_no.setText("JJL001");
	}
	private boolean CheckData(){
		
		username=register_username.getText().toString();
		pwd=register_pwd.getText().toString();
		pwd_confirm=register_pwd_confirm.getText().toString();
		phone=register_phone.getText().toString();
		id=register_id.getText().toString();
		region=register_region.getText().toString();
		address=register_address.getText().toString();
		deviceno=register_device_no.getText().toString();
		addTime=dateFormat.format(new Date());
		logintime=dateFormat.format(new Date());
		 
		if(username.trim().equals("")){
			Toast.makeText(this, "请输入店名!", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if(pwd.trim().equals("")){
			Toast.makeText(this, "请输入密码!", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if(phone.trim().equals("")){
			Toast.makeText(this, "请输入手机号!", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if(id.trim().equals("")){
			Toast.makeText(this, "请输入身份证号!", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if(region.trim().equals("")){
			Toast.makeText(this, "请输入分店区域!", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if(address.trim().equals("")){
			Toast.makeText(this, "请输入具体位置!", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if(deviceno.trim().equals("")){
			Toast.makeText(this, "请输入设备号!", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if(pwd.equals(pwd_confirm)==false){
			Toast.makeText(this, "两次输入密码不匹配，请重新输入!", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		return true;
	}
	

	public void Register(){
		new Thread(){
			public void run() {
				List<Param> list = new ArrayList<OkHttpUtils.Param>();
				list.add(new Param("username", username));
				list.add(new Param("password", pwd));
				list.add(new Param("phonenumber",phone));
				list.add(new Param("idcard",id));
				list.add(new Param("region",region));
				list.add(new Param("address",address));
				list.add(new Param("deviceno",deviceno));
				list.add(new Param("addtime",addTime));
				list.add(new Param("logintime",logintime));
				OkHttpUtils.post(APPConfig.Register, new ResultCallback<String>() {
					public void onSuccess(String response) {
						try{
							JSONObject obj= new JSONObject(response);
							flag=obj.getString("flag");//0: 表示不存在用户  1:表示登陆成功
							if(flag.equals("2")){
								String userid=obj.getString("userid");
								Log.d("Test", userid);
								ApplicationInfo info = (ApplicationInfo) getApplication();
								info.setUserName(username);
								info.setPassword(pwd);
								info.setUserId(userid);
							}
						}catch(JSONException e){
							e.printStackTrace();
							flag=null;
						}
						
						//---------------------
						Message message = new Message();
						if(flag==null){ 						
							message.what = 0;
						}
						else{
							message.what = Integer.parseInt(flag);
						}
						myHandler.sendMessage(message);
						//---------------------
					};
					public void onFailure(Exception e) {
						Message message = new Message();
						message.what = 0;
						myHandler.sendMessage(message);
					};
				}, list);
			};
		}.start();
	}
	
	private void GotoMainAcivity(){
		Intent intent;
		intent=new Intent();
		intent.setClass(RegisterActivity.this, MainActivity.class);
		startActivity(intent);
		RegisterActivity.this.finish();
		overridePendingTransition(R.anim.slide_right_in,
				R.anim.slide_left_out);
	}
	
	@Override
	public void onClick(View v) {
		Intent intent;
		int id = v.getId();
		switch (id) {
		case R.id.registerBack:
			intent=new Intent();
			intent.setClass(RegisterActivity.this, LoginOrRegisterActivity.class);
			startActivity(intent);
			RegisterActivity.this.finish();
			overridePendingTransition(R.anim.slide_left_in,
					                  R.anim.slide_right_out);
			break;
			
		case R.id.register_btn:
			
			if(CheckData()){
				progressDialog.setMessage("注册中...");
				progressDialog.setCanceledOnTouchOutside(false);
		    	progressDialog.show();	
				Register();
			}
			
			break;
			
		default:
			break;
			
		}
		
	}
	
	
	public boolean onKeyDown(int keyCode,KeyEvent event){
		if(keyCode==KeyEvent.KEYCODE_BACK){
			RegisterActivity.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}



//====================================================================
/*if(v==btn_register){
String name=et_user.getText().toString();
String pwd1=et_password.getText().toString();
String pwd2=et_password_checked.getText().toString();

if(name.trim().equals("")){
	Toast.makeText(this, "请输入用户名!", Toast.LENGTH_SHORT).show();
	return;
}
if(pwd1.trim().equals("")){
	Toast.makeText(this, "请输入密码!", Toast.LENGTH_SHORT).show();
	return;
}
if(pwd2.trim().equals("")){
	Toast.makeText(this, "请输入确认密码!", Toast.LENGTH_SHORT).show();
	return;
}

if(pwd1.equals(pwd2)){
	new AlertDialog.Builder(RegisterActivity.this).setTitle("提示").setMessage("注册成功!")
	.setPositiveButton("OK",new DialogInterface.OnClickListener() {					
		@Override
		public void onClick(DialogInterface dialog, int which) {
			Intent intent=new Intent();
			//intent.setClass(RegisterActivity.this, MainActivity.class);
			intent.setClass(RegisterActivity.this, testActivity.class);
			startActivity(intent);
			RegisterActivity.this.finish();
			return;
		}
	}).show();	
}
else{
	Toast.makeText(this, "两次输入密码不匹配，请重新输入!", Toast.LENGTH_SHORT).show();
	return;
}

}
else if(v==btn_cancel){
Intent intent=new Intent();
intent.setClass(RegisterActivity.this, LoginActivity.class);
startActivity(intent);
overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);	
finish();
}*/