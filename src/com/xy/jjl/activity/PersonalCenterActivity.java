package com.xy.jjl.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.xy.jjl.R;
import com.xy.jjl.application.ApplicationInfo;
import com.xy.jjl.common.APPConfig;
import com.xy.jjl.utils.OkHttpUtils;
import com.xy.jjl.utils.OkHttpUtils.Param;
import com.xy.jjl.utils.OkHttpUtils.ResultCallback;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.Toast;
import io.vov.vitamio.utils.Log;

public class PersonalCenterActivity extends Activity implements OnClickListener {
	
	private ImageButton personalCenterBack;
	private Button changepwd;
	private Button quit;
	private TextView usernameTV;
	private TextView idTV;
	private TextView phoneNoTV;
	private TextView regionTV;
	private TextView addressTV;
	private TextView deviceNoTV;
	
	private String username=null;
	private String id=null;
	private String phoneNo=null;
	private String region=null;
	private String address=null;
	private String deviceNo=null;
	
	private String flag;
	private Dialog dialog;
	private Context mContext=PersonalCenterActivity.this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_personal_center);
		ApplicationInfo.getInstance().addActivity(this);
		initView();
		GetPersonalDetail();
	}
	
	public void initView(){
		personalCenterBack=(ImageButton)findViewById(R.id.personalCenterBack);
		changepwd=(Button)findViewById(R.id.center_changepwd_btn);
		quit=(Button)findViewById(R.id.center_quit_btn);
		
		personalCenterBack.setOnClickListener(this);
		changepwd.setOnClickListener(this);
		quit.setOnClickListener(this);
		
		usernameTV=(TextView)findViewById(R.id.center_username);
		idTV=(TextView)findViewById(R.id.center_id);
		phoneNoTV=(TextView)findViewById(R.id.center_phone);
		regionTV=(TextView)findViewById(R.id.center_region);
		addressTV=(TextView)findViewById(R.id.center_address);
		deviceNoTV=(TextView)findViewById(R.id.center_deviceno);
		
	}
	
	private void showDetail(){
 
		if(username!=null)
		{
			usernameTV.setText(username);
		}
		if(id!=null)
		{
			idTV.setText(id);
		}
		
		if(phoneNo!=null)
		{
			phoneNoTV.setText(phoneNo);
		}
		
		if(region!=null)
		{
			regionTV.setText(region);
		}
		
		if(address!=null)
		{
			addressTV.setText(address);
		}
		
		if(deviceNo!=null)
		{
			deviceNoTV.setText(deviceNo);
		}
		
	}
	
	private void GetPersonalDetail(){
		//获取店名
		List<Param> list = new ArrayList<OkHttpUtils.Param>();
		
		String myname=ApplicationInfo.getInstance().getUserName();
		String pwd=ApplicationInfo.getInstance().getPassword();
		list.add(new Param("username", myname));
		list.add(new Param("password", pwd));
		OkHttpUtils.post(APPConfig.personalDetail, new ResultCallback<String>() {
			public void onSuccess(String response) {
				
				try{					
					JSONObject obj= new JSONObject(response);
					flag=obj.getString("flag");//0: 表示不存在用户  1:表示获取用户列表成功
					if(flag.equals("1")){
						username=obj.getString("username");
						id=obj.getString("id");
						phoneNo=obj.getString("phoneNo");
						region=obj.getString("region");
						address=obj.getString("address");
						deviceNo=obj.getString("deviceNo");
					}
				}catch(JSONException e){
					e.printStackTrace();
					flag="0";
				}
				if(flag.equals("0")){
					InitDialog("提示","没有获取到用户信息！");
					dialog.show();
					return;
				}else{
					showDetail();
				}
			};			
			public void onFailure(Exception e) {
				InitDialog("提示","没有获取到用户信息！");
				dialog.show();
				return;
			};
		}, list);
	}
	
	public void InitDialog(String title,String msg){
		
		dialog=new AlertDialog.Builder(mContext).setTitle(title)
			.setMessage(msg)
			.setPositiveButton("OK",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			}).create();

	}
	
	public void onClick(View v){   
		
		Intent intent;
		int id = v.getId();
		switch (id) {
		case R.id.personalCenterBack:
			intent=new Intent();
			intent.setClass(PersonalCenterActivity.this, SettingsActivity.class);
			startActivity(intent);
			PersonalCenterActivity.this.finish();
			overridePendingTransition(R.anim.slide_left_in,
					                  R.anim.slide_right_out);
			break;
			
		case R.id.center_changepwd_btn:
			intent=new Intent();
			intent.setClass(PersonalCenterActivity.this, ChangePasswordActivity.class);
			startActivity(intent);
			PersonalCenterActivity.this.finish();
			overridePendingTransition(R.anim.slide_right_in,
					R.anim.slide_left_out); 
			break;
			
		case R.id.center_quit_btn:
			intent=new Intent();
			intent.setClass(PersonalCenterActivity.this, SettingsActivity.class);
			startActivity(intent);
			PersonalCenterActivity.this.finish();
			overridePendingTransition(R.anim.slide_left_in,
					                  R.anim.slide_right_out);
			break;
		default:
			break;
			
		}
		
	}
}
