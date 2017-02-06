package com.xy.jjl.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.xy.jjl.R;
import com.xy.jjl.application.ApplicationInfo;
import com.xy.jjl.common.APPConfig;
import com.xy.jjl.utils.OkHttpUtils;
import com.xy.jjl.utils.OkHttpUtils.*;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import io.vov.vitamio.utils.Log;


 

public class LoginActivity extends Activity implements OnClickListener {
	
	private ImageButton loginBack;
	private EditText loginUsername;
	private EditText loginPassword;
	private Button loginCmdBtn;
	
	private String username;
	private String password;
	private String logintime;
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private String flag;
	private ProgressDialog progressDialog=null;
	
	private Handler myHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				progressDialog.dismiss();
				StringBuffer sb = new StringBuffer();
				sb.append("登陆失败!");
				Dialog mdialog = new AlertDialog.Builder(LoginActivity.this).setTitle("提示")
						.setMessage(sb.toString())// 提示内容
						.setPositiveButton("确定", null)// 设置确定按钮
						.create();// 创建
				mdialog.show();
				break;
			case 1:
				progressDialog.dismiss();
				GotoMainAcivity();
				break;
			default:
				break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_login);
		ApplicationInfo.getInstance().addActivity(this);
		
		loginBack=(ImageButton)findViewById(R.id.loginBack);
		loginUsername=(EditText)findViewById(R.id.loginUsername);
		loginPassword=(EditText)findViewById(R.id.loginPassword);
		loginCmdBtn=(Button)findViewById(R.id.loginCmdBtn);
		
		loginBack.setOnClickListener(this);
		loginCmdBtn.setOnClickListener(this);
		progressDialog=new ProgressDialog(this);
		
		//Test
		loginUsername.setText("chen");//番禺店  panyudian
		loginPassword.setText("1234");//aaa
	}
	
	private void GotoMainAcivity(){
		Intent intent;
		intent=new Intent();
		intent.setClass(LoginActivity.this, MainActivity.class);
		startActivity(intent);
		LoginActivity.this.finish();
		overridePendingTransition(R.anim.slide_right_in,
				R.anim.slide_left_out);
	}
	
	public void login(){
		new Thread(){
			public void run() {
				List<Param> list = new ArrayList<OkHttpUtils.Param>();
				list.add(new Param("username", username));
				list.add(new Param("password", password));
				list.add(new Param("logintime",logintime));
				OkHttpUtils.post(APPConfig.Login, new ResultCallback<String>() {
					public void onSuccess(String response) {
						try{
							JSONObject obj= new JSONObject(response);
							flag=obj.getString("flag");//0: 表示不存在用户  1:表示登陆成功
							if(flag.equals("1")){
								String userid=obj.getString("userid");
								ApplicationInfo info = (ApplicationInfo) getApplication();
								info.setUserName(username);
								info.setPassword(password);
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
	
	public void onClick(View v){   
		
		
		int id = v.getId();
		switch (id) {
		case R.id.loginBack:
			ApplicationInfo.getInstance().exit();
			finish();
			overridePendingTransition(R.anim.slide_right_in,
					R.anim.slide_left_out);
			break;
		case R.id.loginCmdBtn:
			username=loginUsername.getText().toString();
			password=loginPassword.getText().toString();
			logintime=dateFormat.format(new Date());
						
			if(username.trim().equals("")){
				Toast.makeText(this, "请输入店名", Toast.LENGTH_SHORT).show();
				return;
			}
			if(password.trim().equals("")){
				Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
				return;
			}
			
			progressDialog.setMessage("正在登陆...");
			progressDialog.setCanceledOnTouchOutside(false);
	    	progressDialog.show();	 	    	
			login();
			
			break;
			
		default:
			break;
			
		}
		
	}
		
}
