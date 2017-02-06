package com.xy.jjl.activity;

import com.xy.jjl.R;
import com.xy.jjl.R.dimen;
import com.xy.jjl.R.id;
import com.xy.jjl.R.layout;
import com.xy.jjl.application.ApplicationInfo;
import com.xy.jjl.view.MainView;

import android.R.color;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class testActivity extends Activity {
	private MainView myview=null;
	private float w;
	private float h;
	private int DPI;
	private float density;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainview);
		try{
			int mysize;
			getW();
			getH();
			//System.out.println(getW());
			//System.out.println(getH());
			//getScreenSizeOfDevice();
			myview=(MainView)findViewById(R.id.myview);
			//myview.setBackgroundResource(R.drawable.tmsa_main_test);
			//--------------------		
			mysize=(int)this.getResources().getDimension(R.dimen.Number_size);
			DrawNumber(429f,226f,"0123456789",Color.BLACK,mysize);
			mysize=(int)this.getResources().getDimension(R.dimen.text_size);
			DrawText(385f,105f,"广州万达广场店",Color.BLACK,mysize);
			//--------------------
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void DrawNumber(float mx,float my, String text,int color,int mysize){
		float x=0;
		float y=0;
		x=w*(mx/720f);
		y=h*(my/1280f);
		myview.setNumberXY(x, y);
		myview.setNumberText(text);
		myview.setNumberColor(color);
		myview.setNumberSize(mysize);
		myview.setNumberPaint();
	}

	public void DrawText(float mx,float my, String text,int color,int mysize){
		float x=0;
		float y=0;
		x=w*(mx/720f);
		y=h*(my/1280f);
		myview.setTextXY(x, y);
		myview.setText(text);
		myview.setTextColor(color);
		myview.setTextSize(mysize);
		myview.setTextPaint();
	}
	
	
	@SuppressLint("NewApi")
	public void getScreenSizeOfDevice(){
		Point point=new Point();
		getWindowManager().getDefaultDisplay().getRealSize(point);
		DisplayMetrics dm=getResources().getDisplayMetrics();
		double x=Math.pow(point.x/dm.xdpi, 2);
		double y=Math.pow(point.y/dm.ydpi, 2);
		double screenInches=Math.sqrt(x+y);
		//System.out.println("screenInches=" +screenInches);
	}
	
	public static int dip2px(Context context,float dipValue){
		final float scale=context.getResources().getDisplayMetrics().density;
		return(int)(dipValue*scale+0.5f);
	}
	
	public float getDensity(){
		DisplayMetrics dm=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		density=dm.density;
		return density;
	}
	
	public int getDPI(){
		DisplayMetrics dm=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		DPI=dm.densityDpi;
		return DPI;
	}
	
	public float getW(){
		DisplayMetrics dm=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		w=dm.widthPixels;
		return w;
	}

	public float getH(){
		DisplayMetrics dm=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		h=dm.heightPixels;
		return h;
	}
	
	public void showClickArea(int area){
		Intent intent;
		switch(area){
		case 1:
			Toast.makeText(testActivity.this, "您点击到了成人区域！", Toast.LENGTH_SHORT).show();
			break;
		case 2:
			Toast.makeText(testActivity.this, "您点击到了儿童区域！", Toast.LENGTH_SHORT).show();
			break;
		case 3:
			intent=new Intent();
			intent.setClass(testActivity.this,ImageActivity.class);
			startActivity(intent);
			break;
		case 4:
			intent=new Intent();
			intent.setClass(testActivity.this,SettingsActivity.class);
			startActivity(intent);
			testActivity.this.finish();
			break;
		}
		
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(testActivity.this).
			setTitle("提示").
			setMessage("是否退出系统!").
			setPositiveButton("Yes",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
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
		}
		return true;
	}
}
