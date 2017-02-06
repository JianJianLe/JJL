package com.xy.jjl.application;

import io.vov.vitamio.Vitamio;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

public class ActivityManager extends Application {
	
	public static List<Activity> activityList = new ArrayList<Activity>();
	
	//private List<Activity> activityList=new LinkedList<Activity>();
	private static ActivityManager instance;
	
	public static ActivityManager getInstance(){
		if(instance==null){
			instance=new ActivityManager();
		}
		return instance;
	}
	
	public void addActivity(Activity activity){
		activityList.add(activity);
	}
	
	public void exit(){
		for(Activity activity:activityList){
			if(!activity.isFinishing()){
				activity.finish();
			}
		}
		
		int id=android.os.Process.myPid();
		if(id!=0){
			android.os.Process.killProcess(id);
		}
		
	}
}
