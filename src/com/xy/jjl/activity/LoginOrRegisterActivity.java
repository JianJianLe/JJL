package com.xy.jjl.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;

import com.xy.jjl.R;
import com.xy.jjl.application.ApplicationInfo;
import com.xy.jjl.utils.MoneyUnit;
import com.xy.jjl.utils.PrinterUnit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import io.vov.vitamio.utils.Log;

public class LoginOrRegisterActivity extends Activity implements OnClickListener {
	
	private Button registerBtn;
	private Button loginBtn;
	
	private Context TAG=LoginOrRegisterActivity.this;
	
	//Machine 2017-01-08 - Begin
	//=============================
		/*// 获得收银机工具类的对象
		MoneyUnit mMoneyUnit = new MoneyUnit();
		//初始化Machine:纸钞机
		//-------------------
		public void initMachine(){

			mMoneyUnit.billAcceptorCmdInit();
			mMoneyUnit.billAcceptorDeviceInit();
			mMoneyUnit.billAcceptorConnecting();
			mMoneyUnit.billAcceptorDataGet();

		}*/
	//Machine 2017-01-08 -End
	//==============================================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_or_register);
		ApplicationInfo.getInstance().addActivity(this);
		
		registerBtn=(Button)findViewById(R.id.registerBtn);
		registerBtn.setOnClickListener(this);
		loginBtn=(Button)findViewById(R.id.loginBtn);
		loginBtn.setOnClickListener(this);
		
		//initMachine();
	}
	
 
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		int id = v.getId();
		switch (id) {
		case R.id.loginBtn:
			intent=new Intent();
			intent.setClass(TAG, LoginActivity.class);
			startActivity(intent);
			LoginOrRegisterActivity.this.finish();
			overridePendingTransition(R.anim.slide_right_in,
					R.anim.slide_left_out);
			break;
		case R.id.registerBtn:
			intent=new Intent();
			intent.setClass(TAG, RegisterActivity.class);
			startActivity(intent);
			LoginOrRegisterActivity.this.finish();
			overridePendingTransition(R.anim.slide_right_in,
					R.anim.slide_left_out);
			break;
		default:
			break;
		}
	}
}
