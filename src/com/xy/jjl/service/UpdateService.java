package com.xy.jjl.service;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
 
import com.xy.jjl.R;
import com.xy.jjl.activity.MainActivity;
 
/**
 * 更新应用服务
 * @author Administrator
 *
 */
public class UpdateService extends Service {

	private int titleId=0;//标题
	//下载状态:下载中   下载完成   下载失败
	private final static int DOWNLOADING=0; 
	private final static int DOWNLOAD_COMPLETE=1;
	private final static int DOWNLOAD_FAIL=-1;
	
	public static String downloadUrl = null;//HttpUtil.UploadVersion;//下载链接
	
	//文件存储
	private File updateDir =null;
	private File updateFile=null;
	
	//通知栏
	private NotificationManager updateNotificationManager=null;
	private Notification updateNotification=null;
	
	//点击通知栏通知的 跳转Intent
	private Intent updateIntent =null;
	private PendingIntent updatePendingIntent=null;
	
	private static String notificationTitle="卡族部落";
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
//		Log.i("TAG", " 创建服务onCreate()...");
//		Toast.makeText(this, "创建服务onCreate()...", Toast.LENGTH_SHORT)
//		.show();
		super.onCreate();	 
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
//		Log.i("TAG", " 绑定服务onBind()...");
//		Toast.makeText(this, "绑定服务onBind()...", Toast.LENGTH_SHORT)
//		.show();
		return null;
	}	
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
//		Log.i("TAG", "销毁服务onDestroy()...");
//		Toast.makeText(this, "销毁服务onDestroy()...", Toast.LENGTH_SHORT)
//		.show();
		super.onDestroy();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
//		Log.i("TAT", "开启下载服务onStartCommand...");
//		Toast.makeText(this, "开启下载服务onStartCommand...", Toast.LENGTH_SHORT)
//		.show();
		//获取传值
		titleId =intent.getIntExtra("titleId",0);
		//创建文件
		if(android.os.Environment.MEDIA_MOUNTED.equals(
				android.os.Environment.getExternalStorageState())){
			updateDir=new File(Environment.getExternalStorageDirectory(),
					"app/download/");
			updateFile=new File(updateDir.getPath(),
					getResources().getString(titleId)+ ".apk");
		}
		
		this.updateNotificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		this.updateNotification=new Notification();
		
		//设置下载过程中 点击通知栏  回到某个界面
		updateIntent =new Intent(this, MainActivity.class );
		updatePendingIntent=PendingIntent.getActivity(this,0, updateIntent,0);
		//设置通知栏 显示内容
		updateNotification.icon=R.drawable.ic_launcher;
		updateNotification.tickerText="开始下载";
		updateNotification.setLatestEventInfo(this, notificationTitle , "0%", updatePendingIntent);
		//发出通知
		updateNotificationManager.notify(0, updateNotification);
		
		//开启一个新的线程下载 
		new Thread(new updateRunnable()).start();
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	private Handler updateHandler=new Handler(){
		
		public void handleMessage(Message msg) {
			switch (msg.what) {
			//下载中    更新通知栏的进度提示
			case DOWNLOADING:
//				updateNotification.setLatestEventInfo(UpdateService.this,
//						 notificationTitle, "下载中   "+ msg.arg1 +"%", updatePendingIntent);
				updateNotification.setLatestEventInfo(UpdateService.this,
						 notificationTitle, "下载中...   ", null);
//				updateNotification.setLatestEventInfo(UpdateService.this,
//						notificationTitle, "下载中   "+ msg.arg1 +"%", null);
				updateNotificationManager.notify(0, updateNotification);
				break;
				
			//下载成功  提示安装
			case DOWNLOAD_COMPLETE:
				//下载完直接安装
				updateNotificationManager.cancel(0);
				
				Intent installIntent=new Intent(Intent.ACTION_VIEW);
				Uri uri=Uri.fromFile(updateFile);
				installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				installIntent.setDataAndType(
						uri, "application/vnd.android.package-archive");
				getApplicationContext().startActivity(installIntent);
//				updateNotification.flags|=updateNotification.FLAG_AUTO_CANCEL;
//				//点击安装
//				Intent installIntent=new Intent(Intent.ACTION_VIEW);
//				Uri uri=Uri.fromFile(updateFile);
//				installIntent.setDataAndType(
//						uri, "application/vnd.android.package-archive");
//				updatePendingIntent=PendingIntent.getActivity(UpdateService.this,
//						0, installIntent, 0);
//				updateNotification.defaults=Notification.DEFAULT_LIGHTS;
//				updateNotification.setLatestEventInfo(UpdateService.this,
//						notificationTitle, "下载完成，点击安装", updatePendingIntent);
//				updateNotificationManager.notify(0, updateNotification);
				stopSelf();
//				stopService(new Intent("com.qb.drawingartifact.serviec.UpdateService"));
//				Log.i("TAG", " 下载完成，关闭 服务stopService()...");
				 
				break;
				
			//下载失败
			case DOWNLOAD_FAIL:
				updateNotification.setLatestEventInfo(UpdateService.this,
						notificationTitle, "下载失败", updatePendingIntent);
				updateNotificationManager.notify(0, updateNotification);
				stopService(updateIntent);
				break;
				
			default:
				stopSelf();
				break;
			}
		};
	};
	
	class updateRunnable implements Runnable {

		Message message= updateHandler.obtainMessage();
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
			//增加权限<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE">;  
				if (!updateDir.exists()) {
					updateDir.mkdirs();
				}
				if (updateFile.exists()) {
					updateFile.delete();
				}
				updateFile.createNewFile();
				
                //用到检查升级时从服务获取到的APK下载地址				
				long downloadSize= downloadUpdateFile(downloadUrl , updateFile);
				if(downloadSize>0){
					//下载成功
					message.what=DOWNLOAD_COMPLETE;
					updateHandler.sendMessage(message);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//下载失败
				message.what=DOWNLOAD_FAIL;
				updateHandler.sendMessage(message);
			}			
		}	
	} 
	
	//下载文件
	public long downloadUpdateFile(String downloadUri,File saveFile) throws Exception{
		
		int downloadCount=0;//已经下载百分比数  用于通知栏显示下载百分比
		int currentSize=0;
		long totalSize=0;//文件下载过程中  已经下载的大小
		int updateTotalSize=0;//服务器上的要更新文件的大小
		
		HttpURLConnection httpConnection=null;
		InputStream is=null;
		FileOutputStream fos =null;
		
		try {
			URL url = new URL(downloadUri);
			httpConnection=(HttpURLConnection)url.openConnection();
			
			httpConnection.setRequestProperty("User-Agent", "PacificHttpClient");
			if(currentSize >0){
				httpConnection.setRequestProperty("RANGE", 
						"bytes="+ currentSize + "-");
			}
			
			httpConnection.setConnectTimeout(10000);
			httpConnection.setReadTimeout(20000);
			updateTotalSize=httpConnection.getContentLength();
			
			if(httpConnection.getResponseCode() == 404){
				throw new Exception("fail!");			
			}
			
			is=httpConnection.getInputStream();
			fos=new FileOutputStream(saveFile, false);
			byte buffer[] =new byte[4096];
			int readSize = 0;
			while((readSize=is.read(buffer)) >0){
				fos.write(buffer, 0, readSize);
				totalSize +=readSize;
				//为了防止频繁的通知导致应用吃紧，百分比增加5才通知一次
				if(downloadCount == 0 || 
					 (int) (totalSize*100/updateTotalSize)-5 >downloadCount){
					downloadCount+=5;
					Message message=updateHandler.obtainMessage();
					message.what=DOWNLOADING;
					message.arg1=downloadCount;
					updateHandler.sendMessage(message);
				}
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(httpConnection != null){
				httpConnection.disconnect();
			}
			if(is != null){
				is.close();
			}
			if(fos != null) {
				fos.close();
			}
		}
				
		return totalSize;
	}

}
