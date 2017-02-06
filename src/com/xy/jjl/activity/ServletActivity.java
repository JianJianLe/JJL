package com.xy.jjl.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.xy.jjl.R;
import com.xy.jjl.R.id;
import com.xy.jjl.R.layout;
import com.xy.jjl.application.ApplicationInfo;
import com.xy.jjl.common.APPConfig;
import com.xy.jjl.image_activity.ImageShowActivity;
import com.xy.jjl.utils.HttpUtil;
import com.xy.jjl.utils.OkHttpUtils;
import com.xy.jjl.utils.ProgressDialogUtil;
import com.xy.jjl.utils.OkHttpUtils.Param;
import com.xy.jjl.utils.OkHttpUtils.ResultCallback;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
 
/**
 * @author Administrator
 *
 */
public class ServletActivity extends Activity {
	
	EditText et1=null;
	EditText et2=null;
	Button btn1=null;
	Button btn2=null;
	
	private String username;
	private String pwd;
	private String nowTime;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private String flag;
	private String userId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_servlet);
		
		//ApplicationInfo.getInstance().addActivity(this);
 
		et1=(EditText)findViewById(R.id.et1);
		et2=(EditText)findViewById(R.id.et2);
		
		
		btn1=(Button)findViewById(R.id.btn1);
		btn2=(Button)findViewById(R.id.btn2);
		
		btn1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
			
				/*username=et1.getText().toString();
				pwd=et2.getText().toString();
				nowTime = sdf.format(new Date());
				loginMenberTask loginMember = new loginMenberTask(
						ServletActivity.this);
				loginMember.execute();*/
				try{
				
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(ServletActivity.this, ImageShowActivity.class);
				startActivity(intent);
				finish();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
		});
		
		btn2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				//login();
			}
			
		});
		

	}
 
	public void login(){
		new Thread(){
			public void run() {
				List<Param> list = new ArrayList<OkHttpUtils.Param>();
				list.add(new Param("user_name", "star"));
				list.add(new Param("password", "star"));
				OkHttpUtils.post(APPConfig.Login, new ResultCallback<String>() {
					public void onSuccess(String response) {

						Log.i("TMSW", response);

						Toast.makeText(ServletActivity.this, response, Toast.LENGTH_SHORT);
					};
					public void onFailure(Exception e) {
						Log.d("TMSW", "OKhttp Failure");
					};
				}, list);
			};
		}.start();
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		ApplicationInfo.getInstance().exit();
		finish();
	}
 
	// 正常帐号登录任务
	class loginMenberTask extends AsyncTask<String, Void, String> {
		private Context context;

		loginMenberTask(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(String... params) {
			String resultJson;
			
			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("user_name",
						username));
				nameValuePairs
						.add(new BasicNameValuePair("password", pwd));
				nameValuePairs.add(new BasicNameValuePair("add_time", nowTime));
				
				//发送请求
				resultJson = HttpUtil.httpPost(HttpUtil.Login, nameValuePairs);
				return resultJson;
			} catch (Exception e) {
				return "error";
			}
		}

		@Override
		protected void onPostExecute(String result) {

			ProgressDialogUtil.dismissDialog();
			if (result != null) {
				if (result.equals("error")) {
					Toast.makeText(
							ServletActivity.this,
							"请求超时",
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(ServletActivity.this,
							testActivity.class);
					startActivity(intent);
					/*overridePendingTransition(R.anim.in_from_right,
							R.anim.out_to_left);*/
				} else {
					// 解析返回的json数据
					JSONObject object;
					try {
						object = new JSONObject(result);
						flag = object.getString("flag");
						username = object.getString("userName");
						pwd = object.getString("password");
						userId = object.getString("sessionid");
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if (flag.equals("1")) {					

						/*ApplicationInfo info = (ApplicationInfo) getApplication();
						info.setLoginFlag("loginOK");
						info.setUserId(userId);
						info.setPassword(username);
						info.setUserName(pwd);*/

						Intent intent = new Intent(ServletActivity.this,
								testActivity.class);
						startActivity(intent);
						Toast.makeText(ServletActivity.this, "登陆成功",
								Toast.LENGTH_SHORT).show();
						ServletActivity.this.finish();
					} else {
						Toast.makeText(ServletActivity.this,
								"登陆失败,请确认用户名密码！", Toast.LENGTH_SHORT).show();
					}
				}
			} else {
				Toast.makeText(ServletActivity.this, "登陆失败,请确认用户名密码！",
						Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}
	}
	
	
/*
	private Button registerBtn, loginBtn;
	private LinearLayout autoLoginLL,registerOrLoginLL;
	private String nowTime;
	private String loginName;
	private String loginPass;
	private String userId;
	private String flag;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_or_login);
		ApplicationInfo.getInstance().addActivity(this);
		autoLoginLL = (LinearLayout) findViewById(R.id.autoLoginLL);

		if (PreferenceUtil.getPrefBoolean(RegisterOrLoginActivity.this,
				PreferenceConstants.ISFIRSTSTART, true)) {
			// 第一次启动，进入引导页
			Intent intent = new Intent(RegisterOrLoginActivity.this,
					HelpPageActivity.class);
			startActivity(intent);
			finish(); 
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		} else {
			if (PreferenceUtil.getPrefString(RegisterOrLoginActivity.this,
					PreferenceConstants.PASSWORD, null) != null
					&& getIntent().getBooleanExtra("isneedloginauto", true)) {
				// 密码存在并且不是从登录界面跳转过来 直接登录
				autoLoginLL.setVisibility(View.VISIBLE);
				nowTime = sdf.format(new Date());
				loginName=(PreferenceUtil.getPrefString(RegisterOrLoginActivity.this,
						PreferenceConstants.USERNAME, null));
				loginPass=(PreferenceUtil.getPrefString(RegisterOrLoginActivity.this,
						PreferenceConstants.PASSWORD, null));
				String personal = PreferenceUtil.getPrefString(RegisterOrLoginActivity.this,
						PreferenceConstants.PERSONALID, null);
				ApplicationInfo info = ApplicationInfo.getInstance();
				info.setPersonalID(personal);
				new Thread(new Runnable() {
					@Override
					public void run() {
						int time = 0;
						while (time != 2) {
							try {
								Thread.sleep(1000);
								time = time + 1;
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						loginMenberTask loginMember = new loginMenberTask(
								RegisterOrLoginActivity.this);
						loginMember.execute();
					}
				}).start();
			} else {
				initView();
			}
		}
	}

	private void initView() {
		registerBtn = (Button) findViewById(R.id.registerBtn);
		loginBtn = (Button) findViewById(R.id.loginBtn);
		registerOrLoginLL = (LinearLayout) findViewById(R.id.registerOrLoginLL);
		registerOrLoginLL.setVisibility(View.VISIBLE);

		registerBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(RegisterOrLoginActivity.this,
						CommonMemberRegisterActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.in_from_right,
						R.anim.out_to_left);
			}
		});

		loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Intent intent=new Intent(RegisterOrLoginActivity.this,
				// SelectMobanMessageActivity.class);
				Intent intent = new Intent(RegisterOrLoginActivity.this,
						LoginMemberActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.in_from_right,
						R.anim.out_to_left);
			}
		});
	}

	// 正常帐号登录任务
	class loginMenberTask extends AsyncTask<String, Void, String> {
		private Context context;

		loginMenberTask(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(String... params) {
			String resultJson;

			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("user_name",
						loginName));
				nameValuePairs
						.add(new BasicNameValuePair("password", loginPass));
				nameValuePairs.add(new BasicNameValuePair("add_time", nowTime));

				resultJson = HttpUtil.httpPost(HttpUtil.Login, nameValuePairs);
				return resultJson;
			} catch (Exception e) {
				return "error";
			}
		}

		@Override
		protected void onPostExecute(String result) {

			ProgressDialogUtil.dismissDialog();
			if (result != null) {
				if (result.equals("error")) {
					Toast.makeText(
							RegisterOrLoginActivity.this,
							context.getResources().getString(
									R.string.connect_outtime),
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(RegisterOrLoginActivity.this,
							LoginMemberActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.in_from_right,
							R.anim.out_to_left);
				} else {
					// 解析返回的json数据
					JSONObject object;
					try {
						object = new JSONObject(result);
						flag = object.getString("flag");
						loginName = object.getString("userName");
						loginPass = object.getString("password");
						userId = object.getString("sessionid");
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if (flag.equals("1")) {					

						ApplicationInfo info = (ApplicationInfo) getApplication();
						info.setLoginFlag("loginOK");
						info.setUserId(userId);
						info.setPassword(loginPass);
						info.setUserName(loginName);

						Intent intent = new Intent(RegisterOrLoginActivity.this,
								MainActivity.class);
						startActivity(intent);
						overridePendingTransition(R.anim.in_from_right,
								R.anim.out_to_left);
						Toast.makeText(RegisterOrLoginActivity.this, "登陆成功",
								Toast.LENGTH_SHORT).show();
						RegisterOrLoginActivity.this.finish();
					} else {
						Toast.makeText(RegisterOrLoginActivity.this,
								"登陆失败,请确认用户名密码！", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(RegisterOrLoginActivity.this,
								LoginMemberActivity.class);
						startActivity(intent);
						overridePendingTransition(R.anim.in_from_right,
								R.anim.out_to_left);
					}
				}
			} else {
				Toast.makeText(RegisterOrLoginActivity.this, "登陆失败,请确认用户名密码！",
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(RegisterOrLoginActivity.this,
						LoginMemberActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.in_from_right,
						R.anim.out_to_left);
			}
			super.onPostExecute(result);
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		ApplicationInfo.getInstance().exit();
		finish();
	}	*/

	
}

