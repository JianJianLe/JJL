package com.xy.jjl.application;


import io.vov.vitamio.Vitamio;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.wlf.filedownloader.*;
import org.wlf.filedownloader.FileDownloadConfiguration.Builder;

import android.app.Activity;
import android.app.Application;
import android.os.Environment;

import com.xy.jjl.utils.UncaughtException;

public class ApplicationInfo extends Application {

	private String loginFlag;// 登录标志（是否已经登录）
	private String userId;// 登录成功时的 userId

	private String userName;//
	private String password;//
	
	private String filePath;
	private String VideoPath;
	private String ImagePath;
	
	private String namelist;
	
	private List<Activity> activityList = new LinkedList<Activity>();
	private static ApplicationInfo info;


	public ApplicationInfo() {

	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLoginFlag() {
		return loginFlag;
	}

	public void setLoginFlag(String loginFlag) {
		this.loginFlag = loginFlag;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getNameList(){
		return namelist;
	}
	
	public void setNameList(String namelist){
		this.namelist=namelist;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		loginFlag = "OK";
		userId = "";
		info = this;

//		UncaughtException mUncaughtException = UncaughtException.getInstance();
//		mUncaughtException.init();
 
		
		//初始化Vitomio
		Vitamio.isInitialized(ApplicationInfo.this);
		
		//初始化FileDownloader
		InitFileDownloader();
		
	}

	private void creatImageFolder(){
		File file=null;
		file=Environment.getExternalStorageDirectory();
		filePath=file.getAbsolutePath()+File.separator+"JJL";
		file=new File(filePath);
		if(!file.exists()){
			file.mkdirs();
		}
	}
	
	public String getFilePath(){
		File file=null;
		file=Environment.getExternalStorageDirectory();
		filePath=file.getAbsolutePath()+File.separator+"JJL";
		file=new File(filePath);
		if(!file.exists()){
			file.mkdirs();
		}
		return this.filePath;
	}
	
	public String getVideoPath(){
		File file=null;
		file=Environment.getExternalStorageDirectory();
		VideoPath=file.getAbsolutePath()+File.separator+"JJL"+File.separator+"Video";
		file=new File(VideoPath);
		if(!file.exists()){
			file.mkdirs();
		}
		return this.VideoPath;
	}
	
	public String getImagePath(){
		File file=null;
		file=Environment.getExternalStorageDirectory();
		ImagePath=file.getAbsolutePath()+File.separator+"JJL"+File.separator+"Image";
		file=new File(ImagePath);
		if(!file.exists()){
			file.mkdirs();
		}
		return this.ImagePath;
	}
	
	private void InitFileDownloader(){
		Builder builder=new FileDownloadConfiguration.Builder(this);
		creatImageFolder();
		builder.configFileDownloadDir(filePath);//配置下载文件保存的文件夹
		builder.configDownloadTaskSize(3);//配置同事下载任务数量,默认为2
		builder.configConnectTimeout(25000);//配置连接网络超时时间,25秒
		FileDownloadConfiguration configuration=builder.build();
		FileDownloader.init(configuration);
	}
	
	public synchronized static ApplicationInfo getInstance() {
		if (null == info) {
			info = new ApplicationInfo();
		}
		return info;
	}

	// add Activity
	public void addActivity(Activity activity) {
		activityList.add(activity);

		UncaughtException.getInstance().setContext(activity);
	}

	public void exit() {
		try {
			for (Activity activity : activityList) {
				if (activity != null)
					activity.finish();
			}
			
			int id=android.os.Process.myPid();
			if(id!=0){
				android.os.Process.killProcess(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}
  

}