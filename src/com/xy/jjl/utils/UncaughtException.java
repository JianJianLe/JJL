package com.xy.jjl.utils;


import java.lang.Thread.UncaughtExceptionHandler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;

//import com.wc.xph.LoginMemberActivity;
import com.xy.jjl.application.ApplicationInfo;

/**
 * 捕获全局异常,因为有的异常我们捕获不到
 * 
 * @author river
 * 
 */
public class UncaughtException implements UncaughtExceptionHandler {
	private final static String TAG = "TAG";
	private static UncaughtException mUncaughtException;
	private Context context;

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	private UncaughtException() {
	}

	/**
	 * 同步方法，以免单例多线程环境下出现异常
	 * 
	 * @return
	 */
	public synchronized static UncaughtException getInstance() {
		if (mUncaughtException == null) {
			mUncaughtException = new UncaughtException();
		}
		return mUncaughtException;
	}

	/**
	 * 初始化，把当前对象设置成UncaughtExceptionHandler处理器
	 */
	public void init() {
		Thread.setDefaultUncaughtExceptionHandler(mUncaughtException);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// 处理异常,我们还可以把异常信息写入文件，以供后来分析。
		Log.e(TAG,
				"uncaughtException thread : " + thread + "||name="
						+ thread.getName() + "||id=" + thread.getId()
						+ "||exception=" + ex);
		showDialog();
	}

	private void showDialog() {
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				new AlertDialog.Builder(context).setTitle("提示")
						.setCancelable(false).setMessage("未知错误！")
						.setNeutralButton("我知道了", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								//Intent intent=new Intent(context,LoginMemberActivity.class);
								ApplicationInfo info = (ApplicationInfo)ApplicationInfo.getInstance();
								info.setLoginFlag("Exit");
								ApplicationInfo.getInstance().exit();
							}
						}).create().show();
				Looper.loop();
			}
		}.start();
	}
}
