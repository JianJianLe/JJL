package com.xy.jjl.utils;


import org.json.JSONException;
import org.json.JSONObject;

import com.xy.jjl.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.xy.jjl.service.*;
//更新应用管理类
public class UpdateManager {

	private static Context mContext;
	private static String newVerName = "";
	private static int newVerCode =0;
	//是否  弹出不用更新对话框    进入主活动检查更新时   不需要更新时就不弹出对话框
	private static boolean isShowNotUpdateDialog=true;

	public UpdateManager(Context context,boolean isShowNotUpdateDialog) {
		this.mContext = context;
		this.isShowNotUpdateDialog=isShowNotUpdateDialog;
	}

	// 获取当前版本代码
	private static int getCurrentVerCode() {
		int verCode = -1;
		try {
			// 获取当前版本代码
			verCode = mContext.getPackageManager().getPackageInfo(
					mContext.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			Log.i("TAG", e.getMessage());
		}
		return verCode;
	}

	// 获取当前版本的名称
	private static String getCurrentVerName() {
		String verName = "";
		try {
			verName = mContext.getPackageManager().getPackageInfo(
					mContext.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			Log.i("TAG", e.getMessage());
		}
		return verName;
	}

	// 获取 应用名称
	private static String getCurrentAppName() {
		String appName = mContext.getResources().getText(R.string.app_name)
				.toString();
		return appName;
	}
	
	// 检查是否要更新 （就是比较当前版本 与服务器版本大小）
	public void checkUpdate() {
		CheckServerVersionCodeTask check = new CheckServerVersionCodeTask();
		check.execute();
	}

	class CheckServerVersionCodeTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			//在主活动中检查更新不用显示进度框
			if(isShowNotUpdateDialog){
				ProgressDialogUtil.showProgressDialog(mContext, "", "正在检查版本...");
			}			
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				String resultJson = null;//HttpUtil.httpGet(HttpUtil.CheckServerCode);
				return resultJson;
			} catch (Exception e) {
				return "error";
			}
		}

		@Override
		protected void onPostExecute(String result) {
			if(isShowNotUpdateDialog){
				ProgressDialogUtil.dismissDialog();
			}			
			if (result.isEmpty()) {
				Toast.makeText(mContext, "获取服务器版本信息失败", Toast.LENGTH_SHORT)
						.show();
				newVerCode = -1;
				newVerName = "";
			} else if (result.equals("error")) {
				Toast.makeText(mContext, "获取服务器版本信息失败", Toast.LENGTH_SHORT)
						.show();
				newVerCode = -1;
				newVerName = "";
			} else {
				try {
					JSONObject jsonObject = new JSONObject(result);
					newVerCode = Integer.parseInt(jsonObject.getString("serverVersion"));
//					Log.i("TAG", "服务器版本："+newVerCode);
					
					int curVerCode = getCurrentVerCode();// 获取当前版本
//					String curVerName = getCurrentVerName();// 获取当前版本号
//					Log.i("TAG", "当前版本："+curVerCode);					 
					 
					if ( newVerCode > curVerCode) {
						// 提示要更新
						showNewVersionUpdateDialog();
					} else if(isShowNotUpdateDialog){						 
						// 提示已是最高版本 不用更新
						showNoNewVersionDialog();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			super.onPostExecute(result);
		}
	}

	// 不用更新提示对话框
	public static void showNoNewVersionDialog() {
		StringBuffer sb = new StringBuffer();
		sb.append("已是最新版，无需更新！");
		Dialog dialog = new AlertDialog.Builder(mContext).setTitle("软件更新")
				.setMessage(sb.toString())// 提示内容
				.setPositiveButton("确定", null)// 设置确定按钮
				.create();// 创建
		dialog.show();
	}

	private static void showNewVersionUpdateDialog() {
//		int verCode = getCurrentVerCode();
//		String verName = getCurrentVerName();
		StringBuffer sb = new StringBuffer();
//		sb.append("当前版本：");
//		sb.append(verName);
		sb.append("发现新版本，是否更新?");
		Dialog dialog = new AlertDialog.Builder(mContext).setTitle("软件更新")
				.setMessage(sb.toString())
				.setPositiveButton("更新", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						// 做更新动作 下载并在在通知栏显示下载进度
						Intent intent = new Intent(mContext,
								UpdateService.class);
						intent.putExtra("titleId", R.string.app_name);
						mContext.startService(intent);
					}
				}).setNegativeButton("暂不更新", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				}).create();

		dialog.show();
	}

}

