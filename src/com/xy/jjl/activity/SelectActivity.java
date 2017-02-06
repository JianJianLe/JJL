package com.xy.jjl.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xy.jjl.R;
import com.xy.jjl.application.ApplicationInfo;
import com.xy.jjl.utils.MoneyUnit;
import com.xy.jjl.utils.PrinterUnit;
/**
 * 选择买票界面
 * @author jat
 *
 */
 
public class SelectActivity extends Activity implements OnClickListener{

	private LinearLayout ll_main;
	private TextView tv_time;
	private TextView tv_date;
	private ImageView iv_select_setting;
	private TextView adult;
	private TextView child;
	//格式化时间
	SimpleDateFormat format = new SimpleDateFormat("HH:mm");
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年M月d日");
	private int time = 0;
	private ScheduledExecutorService scheduledExecutorService;
	
	private Context TAG = SelectActivity.this;
	private Context mContext=SelectActivity.this;
	
	private Dialog dialog;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				tv_time.setText(format.format(new Date()));
				tv_date.setText(dateFormat.format(new Date()));
				//30个时间段没有操作的话就回到mainActivity
				if (time==10) {
					SelectActivity.this.startActivity(new Intent(SelectActivity.this, MainActivity.class));
					SelectActivity.this.finish();
					//关掉延迟服务
					scheduledExecutorService.shutdown();
				}else {
					//时间加1
					time++;
				}
				break;

			default:
				break;
			}

		};
	};
 		
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select);
		
		ApplicationInfo.getInstance().addActivity(this);
 
		initView();
		
	}
	

	
	//初始化界面
	public void initView(){
		ll_main = (LinearLayout) findViewById(R.id.ll_main);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_date = (TextView) findViewById(R.id.tv_date);
		ll_main.setOnClickListener(this);
		tv_time.setText(format.format(new Date()));
		tv_date.setText(dateFormat.format(new Date()));
		
		adult=(TextView)findViewById(R.id.adultSelect);
		child=(TextView)findViewById(R.id.childSelect);
		adult.setOnClickListener(this);
		child.setOnClickListener(this);
		
		iv_select_setting = (ImageView) findViewById(R.id.iv_select_setting);
		iv_select_setting.setOnClickListener(this);
		
	}
	/**
	 * 换行定时任务
	 * 
	 * @author Administrator
	 * 
	 */
	private class TimeTask implements Runnable {

		public void run() {
			Message message = new Message();
			message.what = 0;
			handler.sendMessage(message);
		}
	}

	/**
	 * 开启服务
	 */
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleWithFixedDelay(new TimeTask(), 1, 1, TimeUnit.SECONDS);
	}
	
	//关掉延迟服务
	@Override
	public void onDestroy(){
		// TODO Auto-generated method stub
		super.onDestroy();
		scheduledExecutorService.shutdown();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.ll_main:
			//触摸屏幕，操作的时候将time调为0
			time = 0;
			break;
		
		case R.id.iv_select_setting:
			TAG.startActivity(new Intent(TAG,SettingsActivity.class));
			((Activity) TAG).finish();
			break;
		
		case R.id.adultSelect:
			DialogShow(1);
			break;
			
		case R.id.childSelect:
			DialogShow(2);
			break;
			
		default:
			break;
		}
	}
	
	private void DialogShow(int flag){
		if(flag==1){
		//Toast.makeText(SelectActivity.this, "您选择了成人票,请支付15元", Toast.LENGTH_LONG);
			InitDialog("提示","您选择了成人票,请支付15元");
			dialog.show();
			
		}
		else if(flag==2){
		//Toast.makeText(SelectActivity.this, "您选择了儿童票,请支付10元", Toast.LENGTH_LONG);
			InitDialog("提示","您选择了儿童票,请支付10元");
			dialog.show();
		}
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
	
	public boolean onKeyDown(int keyCode,KeyEvent event){
		new AlertDialog.Builder(SelectActivity.this).
		setTitle("提示").
		setMessage("是否退出系统!").
		setPositiveButton("Yes",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				scheduledExecutorService.shutdown();
				
				ApplicationInfo.getInstance().exit();
				
				/*for(int i=0;i<ActivityManager.activityList.size();i++){
					if (null != ActivityManager.activityList.get(i)) {
						ActivityManager.activityList.get(i).finish();
					}
				}
				System.exit(0);*/
				return;
			}
		}).
		setNegativeButton("No", new DialogInterface.OnClickListener() {   
            
             @Override   
             public void onClick(DialogInterface dialog, int which) {   
                 // TODO Auto-generated method stub    
            	 return;
             }   
		}).show();
		return super.onKeyDown(keyCode, event);
	}
	
	
	
	
		
}
