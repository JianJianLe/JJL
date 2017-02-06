package com.xy.jjl.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.wlf.filedownloader.*;
import org.wlf.filedownloader.listener.OnDeleteDownloadFileListener;
import org.wlf.filedownloader.listener.OnDetectBigUrlFileListener;
import org.wlf.filedownloader.listener.OnDownloadFileChangeListener;
import org.wlf.filedownloader.listener.OnFileDownloadStatusListener;
import org.wlf.filedownloader.listener.OnMoveDownloadFileListener;
import org.wlf.filedownloader.listener.simple.OnSimpleFileDownloadStatusListener;

//---------
import com.squareup.okhttp.*;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;
//---------

import com.xy.jjl.R;
import com.xy.jjl.R.anim;
import com.xy.jjl.R.id;
import com.xy.jjl.R.layout;
import com.xy.jjl.application.ApplicationInfo;
import com.xy.jjl.common.APPConfig;
import com.xy.jjl.image_activity.ImageShowActivity;
import com.xy.jjl.utils.CallbackBundle;
import com.xy.jjl.utils.FolderUtil;
import com.xy.jjl.utils.OkHttpUtils;
import com.xy.jjl.utils.OkHttpUtils.Param;
import com.xy.jjl.utils.OkHttpUtils.ResultCallback;
import com.xy.jjl.utils.DeleteFileUtil;
import com.xy.jjl.utils.OpenFileDialog;
import com.xy.jjl.utils.UpdateManager;
import com.xy.jjl.utils.UriUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.InsetDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity implements OnClickListener{

	private TextView img_upload;
	private ImageButton settingBack;
	private TextView settingCenter;
	private TextView settingAboutUsTextView;
	private TextView settingDisclaimerTextView;
	private TextView settingVideo;
	private TextView UpdateVersion;
	private TextView discountSet;
	private TextView billSearch;
	private TextView exceptionSearch;
	
	
	static private int openfileDialogId = 0;
	
	private String u_name;
	private int uid;
	private final int IMAGE_CODE=0;
	private String ImagePath=null;
	public ProgressDialog myDialog;
	private Context mContext=SettingsActivity.this;
	final int LeftToRight=0;
	final int RightToLeft=1;
	
	private String[] proj={MediaStore.Video.Media.DATA};
	//-----------
	private String v_path=null; 
	private String v_size=null; 
	private String v_name=null;
	//-----------
	
	private static final OkHttpClient okHttpClient = new OkHttpClient();
	
	private Dialog dialog;
	//-----
	//progressbar
	private Handler mHandler;
	private ProgressDialog progressDialog=null;
	private static final int MAX_PROGRESS=100;
	private static final int PROGRESSDIALOG_FLAG=1;
	private int progress=0;
	private String downloadVideoUrl=null;
	private String downloadImageUrl=null;
	//-----
	
	//----------
	private LinearLayout downloadvideo;
	//----------
	
	private String flag;
	//----------
	private Dialog namelistDialog;
	private String namelisttemp;
	private String[] namelist;
	private boolean[] arraySelected;
	//private String[] namelist=new String[]{"��خ��","���ŵ�","���ϵ�","ʯ����"};
	//private boolean[] arraySelected=new boolean[] {false,false,false,false};
	private StringBuilder selectedName;
	private String mynamelist;	
	//-----------
	private OnFileDownloadStatusListener mOnFileDownloadStatusListener;
	private OnDownloadFileChangeListener mOnDownloadFileChangeListener;
	private OnDeleteDownloadFileListener videoOnDeleteDownloadFileListener;
	private OnDeleteDownloadFileListener imageOnDeleteDownloadFileListener;
	private OnDeleteDownloadFileListener updateOnDeleteDownloadFileListener;
	private OnMoveDownloadFileListener onMoveDownloadFileListener;
	//-----------
	private String filePath=null;
	private String videoname=null;
	private String videopath=null;
	private String imagename=null;
	private String imagepath=null;
	private String[] imagenamelist=null;
	private String newFileDir=null;
	private String newVideoFileDir=null;
	private String newImageFileDir=null;
	private String videoUrl=null;
	private String imageUrl=null;
	
	private int namelistLen=0;
	private int nLen=0;
	//-----------
	
	//----------
	private Dialog updateDialog;
	private String apkurl=null;
	private String newAPKFileDir=null;
	private String apkPath=null;
	//----------
	
	private DecimalFormat df=new DecimalFormat(".##");
	
	@SuppressLint("HandlerLeak")
	public Handler myHandler=new Handler(){
		@Override
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			
			Bundle b;
			String url;
			
			switch(msg.what){	
			
			case 0:		
				InitDialog("Update","�������!");
				break;	
			case 1:
				InitDialog("Send","��Ƶ�ϴ��ɹ�!");	
				break;
			case 2:
				progressDialog.dismiss();
				InitDialog("Download","��Ƶ�������!");
				break;
			case 3:
				InitDialog("Download","����ʧ��!");
				break;
				
			case 4:
				b=msg.getData();
				url=b.getString("url");	
				videoUrl=url;
				FileDownloader.start(url);
				break;
				
			case 5:
				b=msg.getData();
				url=b.getString("url");	
				FileDownloader.start(url);
				break;
			
			case 6:
				installApk(apkPath);
				break;
				
			case 7:
				if(nLen==namelistLen){
					progressDialog.dismiss();
					InitDialog("Download","ͼƬ�������!");
				}else if(nLen<namelistLen){	
					
					//Log.d("image", "len=" + nLen);
					//Log.d("image", imagenamelist[nLen]);
					//Log.d("image", "namelistlen=" + namelistLen);
 
					nLen++;
					downloadImageUrl=APPConfig.BaseLink + imagepath + imagenamelist[nLen];
					//��ɾ�����أ�Ȼ����Handler������������
					FileDownloader.delete(downloadImageUrl, true, imageOnDeleteDownloadFileListener);	
					
				}
				break;
			case 8:
				b=msg.getData();
				url=b.getString("url");	
				imageUrl=url;
				FileDownloader.start(url);
				break;
 
			}
			
		}
	};
	
 
	public void InitDialog(String title,String msg){
		
		dialog=new AlertDialog.Builder(mContext).setTitle(title)
			.setMessage(msg)
			.setPositiveButton("OK",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			}).create();
		dialog.show();
	}
 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		ApplicationInfo.getInstance().addActivity(this);
		
		img_upload=(TextView)findViewById(R.id.settingImage);
		img_upload.setOnClickListener(this);
		//========
		settingBack=(ImageButton) findViewById(R.id.settingBack);
		settingBack.setOnClickListener(this);
		//=======
		
		settingCenter=(TextView)findViewById(R.id.settingCenter);
		settingCenter.setOnClickListener(this);
		
		settingVideo=(TextView)findViewById(R.id.settingVideo);
		settingVideo.setOnClickListener(this);
		
		UpdateVersion=(TextView)findViewById(R.id.settingUpdateVersion);
		UpdateVersion.setOnClickListener(this);
		
		settingAboutUsTextView=(TextView)findViewById(R.id.settingAboutUsTextView);
		settingAboutUsTextView.setOnClickListener(this);
		
		settingDisclaimerTextView=(TextView)findViewById(R.id.settingDisclaimerTextView);
		settingDisclaimerTextView.setOnClickListener(this);
        
		progressDialog=new ProgressDialog(this);
		
		downloadvideo=(LinearLayout) findViewById(R.id.download_video);
		
		discountSet=(TextView)findViewById(R.id.discountSet);
		discountSet.setOnClickListener(this);
		
		billSearch=(TextView)findViewById(R.id.billSearch);
		billSearch.setOnClickListener(this);
		
		exceptionSearch=(TextView)findViewById(R.id.exceptionSearch);
		exceptionSearch.setOnClickListener(this);
		
		//ע��filedownloader������
		InitFileDownloadListener();
		
		CreateFolder();
	}
	 
	
	public void openActivity(Context context, Class<?> cls, int flag,boolean isfinish){
		Intent intent=new Intent();
		intent.setClass(context, cls);
		startActivity(intent);
		if(flag==LeftToRight){
			//����ҳ� LeftToRight 0
			overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);	
		}
		else if(flag==RightToLeft){
			//�ҽ���� RightToLeft 1
			overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);	
		}
		if(isfinish){
			finish();
		}
	}
	public void onClick(View v){
		StringBuffer sb;
		Dialog mdialog;
		
		downloadImageUrl=null;
		downloadVideoUrl=null;
		apkurl=null;
		nLen=0;
		
		int id = v.getId();
		switch (id) {
		
		case R.id.settingBack:
			openActivity(mContext,MainActivity.class,LeftToRight,true);
			break;
		
		case R.id.settingCenter:
			openActivity(mContext,PersonalCenterActivity.class,RightToLeft,true);
			break;
			
		case R.id.settingImage:
			//openActivity(mContext,ImageShowActivity.class,RightToLeft,true);
			AlertDialog.Builder imgbuilder=new Builder(mContext);
			imgbuilder.setItems(getResources().getStringArray(R.array.image), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					//System.out.println(which);
					if(which==0){
						dialog.dismiss();
						//showListDialog("��ѡ��Ҫ�������ͼƬ�ĵ���","image");
						//openActivity(mContext,ImageShowActivity.class,RightToLeft,true);
						getNameList("image");
					}
					if(which==1){
						dialog.dismiss();
						
						//Delete image files
						//----------
						//newImageFileDir
						DeleteFileUtil.deleteDirectory(newImageFileDir);
						//----------
						//--------------
						//��ȡ��Ƶ��Ϣ
						List<Param> list = new ArrayList<OkHttpUtils.Param>();
						String userid=ApplicationInfo.getInstance().getUserId();
						String username=ApplicationInfo.getInstance().getUserName();
						list.add(new Param("username", username));
						list.add(new Param("userid", userid));
						OkHttpUtils.post(APPConfig.imageDetail, new ResultCallback<String>() {
							public void onSuccess(String response) {
								
								try{
									String key;
									String value;
									StringBuilder tempStr;
									JSONObject json;
									JSONObject obj= new JSONObject(response);
									flag=obj.getString("flag");//0: ��ʾ�������û�  1:��ʾ��ȡ�û��б�ɹ�
									if(flag.equals("1")){
										imagename=obj.getString("imagename");
										imagepath=obj.getString("imagepath");										
									}
								}catch(JSONException e){
									e.printStackTrace();
									flag="0";
								}
								if(flag.equals("0")){
									InitDialog("��ʾ","û�л�ȡ��ͼƬ��Ϣ��");									
									return;
								}else{
									imagenamelist=imagename.split(",");									
									namelistLen=imagenamelist.length-1;
									Log.d("image", "imagename="+imagename);
									Log.d("image", "namelistLen = "+namelistLen);
									if(nLen<namelistLen){									
										downloadImageUrl=APPConfig.BaseLink + imagepath + imagenamelist[nLen];
										//��ɾ�����أ�Ȼ����Handler������������
										FileDownloader.delete(downloadImageUrl, true, imageOnDeleteDownloadFileListener);	
									}
								}
							};
							public void onFailure(Exception e) {
								InitDialog("��ʾ","û�л�ȡ��ͼƬ��Ϣ��");
								return;
							};
						}, list);
	
					}
				}
			});
			imgbuilder.show();
			break;
			
		case R.id.settingVideo:
			AlertDialog.Builder videobuilder=new Builder(mContext);
			videobuilder.setItems(getResources().getStringArray(R.array.video), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					//System.out.println(which);
					if(which==0){
						dialog.dismiss();
						getNameList("video");
					}
					if(which==1){
						dialog.dismiss();
						//String videoname="T1.mp4";
						//downloadVideoUrl=APPConfig.BaseLink + "Video/T1.mp4";
						//url=APPConfig.BaseLink+"Video/T2.flv";
						
						//--------------
						//��ȡ��Ƶ��Ϣ
						List<Param> list = new ArrayList<OkHttpUtils.Param>();
						String userid=ApplicationInfo.getInstance().getUserId();
						String username=ApplicationInfo.getInstance().getUserName();
						list.add(new Param("username", username));
						list.add(new Param("userid", userid));
						OkHttpUtils.post(APPConfig.videoDetail, new ResultCallback<String>() {
							public void onSuccess(String response) {
								
								try{
									String key;
									String value;
									StringBuilder tempStr;
									JSONObject json;
									JSONObject obj= new JSONObject(response);
									flag=obj.getString("flag");//0: ��ʾ�������û�  1:��ʾ��ȡ�û��б�ɹ�
									if(flag.equals("1")){
										videoname=obj.getString("videoname");
										videopath=obj.getString("videopath");										
									}
								}catch(JSONException e){
									e.printStackTrace();
									flag="0";
								}
								if(flag.equals("0")){
									InitDialog("��ʾ","û�л�ȡ����Ƶ��Ϣ��");									
									return;
								}else{
									downloadVideoUrl=APPConfig.BaseLink + videopath + videoname;
									//��ɾ�����أ�Ȼ����Handler������������
									FileDownloader.delete(downloadVideoUrl, true, videoOnDeleteDownloadFileListener);
								}
							};
							public void onFailure(Exception e) {
								InitDialog("��ʾ","û�л�ȡ����Ƶ��Ϣ��");
								return;
							};
						}, list);
						
						//��ɾ�����أ�Ȼ����Handler������������
						//FileDownloader.delete(downloadVideoUrl, true, videoOnDeleteDownloadFileListener);
						
						//List<String> urls=new ArrayList<String>();						
						//urls.add(APPConfig.BaseLink + "Video/T1.mp4");
						//urls.add(APPConfig.BaseLink + "Video/T2.flv");
						// ����ļ�û�����ع������������������أ�����������أ��Զ���ϵ�����������������޷�֧�ֶϵ���������ͷ��ʼ���أ�
						//FileDownloader.start(url);
						//FileDownloader.start(urls);

					}
				}
			});
			videobuilder.show();
			
			break;
		
		case R.id.discountSet:
			sb = new StringBuffer();
			sb.append("�ۿ����ù�����δ���ƣ�");
			mdialog = new AlertDialog.Builder(mContext).setTitle("��ʾ")
					.setMessage(sb.toString())// ��ʾ����
					.setPositiveButton("ȷ��", null)// ����ȷ����ť
					.create();// ����
			mdialog.show();
			break;
			
		case R.id.billSearch:
			sb = new StringBuffer();
			sb.append("�˵���ѯ������δ���ƣ�");
			mdialog = new AlertDialog.Builder(mContext).setTitle("��ʾ")
					.setMessage(sb.toString())// ��ʾ����
					.setPositiveButton("ȷ��", null)// ����ȷ����ť
					.create();// ����
			mdialog.show();
			break;
		
		case R.id.exceptionSearch:
			sb = new StringBuffer();
			sb.append("�쳣��ѯ������δ���ƣ�");
			mdialog = new AlertDialog.Builder(mContext).setTitle("��ʾ")
					.setMessage(sb.toString())// ��ʾ����
					.setPositiveButton("ȷ��", null)// ����ȷ����ť
					.create();// ����
			mdialog.show();
			break;
			
		case R.id.settingUpdateVersion:
			
			showUpdateDialog();
			//UpdateManager updateManager=new UpdateManager(mContext,true);
			//updateManager.showNoNewVersionDialog();
			break;
			
		case R.id.settingAboutUsTextView:
			openActivity(mContext,AboutUsActivity.class,RightToLeft,false);				
			break;
			
		case R.id.settingDisclaimerTextView:
			openActivity(mContext,DisclaimerActivity.class,RightToLeft,false);
			break;
		default:
			break;
		}
	}
	
	//��װapk   
	public void installApk(String path) {  
		File file=new File(path);
	    Intent intent = new Intent();  
	    //ִ�ж���  
	    intent.setAction(Intent.ACTION_VIEW);  
	    //ִ�е���������  
	    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");  
	    startActivity(intent);  
	}  
	
	private void getNameList(final String typeStr){
		
		List<Param> list = new ArrayList<OkHttpUtils.Param>();
		
		String userid=ApplicationInfo.getInstance().getUserId();
		String username=ApplicationInfo.getInstance().getUserName();
		String pwd=ApplicationInfo.getInstance().getPassword();
		list.add(new Param("username", username));
		list.add(new Param("password", pwd));
		list.add(new Param("userid", userid));
		OkHttpUtils.post(APPConfig.nameListUrl, new ResultCallback<String>() {
			public void onSuccess(String response) {
				
				try{
					String key;
					String value;
					StringBuilder tempStr;
					JSONObject json;
					JSONObject obj= new JSONObject(response);
					flag=obj.getString("flag");//0: ��ʾ�������û�  1:��ʾ��ȡ�û��б�ɹ�
					if(flag.equals("1")){
						namelisttemp=obj.getString("namelist");
						JSONArray array=new JSONArray(namelisttemp);
						tempStr=new StringBuilder();
						for(int i=0;i<array.length();++i){
							json=array.getJSONObject(i);
							Iterator it=json.keys();
							while(it.hasNext()){
								key=it.next().toString();
								value=json.getString(key);
								tempStr.append(value+",");
							}
						}
						
						String str=tempStr.toString();
						str=str.substring(0,str.length()-1);
						namelist=str.split(",");	
						arraySelected=new boolean[namelist.length];
						for(int i=0;i<arraySelected.length;i++){
							arraySelected[i]=false;
						}
					}
				}catch(JSONException e){
					e.printStackTrace();
					flag="0";
				}
				if(flag.equals("0")){
					InitDialog("��ʾ","û�л�ȡ���û��б���Ϣ��");									
					return;
				}else{	
					if(typeStr.equals("video")){
						showListDialog("��ѡ��Ҫ���������Ƶ�ĵ���","video");
					}else if(typeStr.equals("image")){
						showListDialog("��ѡ��Ҫ�������ͼƬ�ĵ���","image");
					}
				}
			};
			@SuppressLint("ShowToast")
			public void onFailure(Exception e) {
				InitDialog("��ʾ","û�л�ȡ���û��б���Ϣ��");
				return;
			};
		}, list);
	}
	
	private void showUpdateDialog(){
		updateDialog=new AlertDialog.Builder(this).
		setTitle("��ʾ").
		setMessage("�Ƿ�Ҫ�������°汾��").
		setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//��ȡ��Ƶ��Ϣ
				List<Param> list = new ArrayList<OkHttpUtils.Param>();
				String username=ApplicationInfo.getInstance().getUserName();
				list.add(new Param("username", username));
				OkHttpUtils.post(APPConfig.updateAPK, new ResultCallback<String>() {
					public void onSuccess(String response) {
						
						try{
							JSONObject obj= new JSONObject(response);
							flag=obj.getString("flag");//0: ��ʾ�������û�  1:��ʾ��ȡ�û��б�ɹ�
						}catch(JSONException e){
							e.printStackTrace();
							flag="0";
						}
						if(flag.equals("0")){
							InitDialog("��ʾ","�Ѿ������°汾�ˣ�");									
							return;
						}else{
							apkurl=APPConfig.APKlink;
							//��ɾ�����أ�Ȼ����Handler������������
							FileDownloader.delete(apkurl, true,updateOnDeleteDownloadFileListener);
						}
					};
					public void onFailure(Exception e) {
						InitDialog("��ʾ","û�����سɹ���");
						return;
					};
				}, list);

			}
		}).
		setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {   
            
             @Override   
             public void onClick(DialogInterface dialog, int which) {   
                      
            	 return;
             }   
		}).create();
		updateDialog.show();
	}
	
	private void showListDialog(String title,final String mtype){

		namelistDialog=new AlertDialog.Builder(this).setTitle(title)
			.setMultiChoiceItems(namelist,arraySelected, new DialogInterface.OnMultiChoiceClickListener() {
			
			@Override                                                 
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				// TODO Auto-generated method stub
				arraySelected[which]=isChecked;
			}
		}).setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			
			@SuppressLint("ShowToast")
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				selectedName=new StringBuilder();
				for(int i=0;i<arraySelected.length;i++){
					if(arraySelected[i]==true){
						selectedName.append(namelist[i]+",");
					}
				}
				String str=selectedName.toString();
				str=str.substring(0,str.length()-1);
				mynamelist=str;
				
				ApplicationInfo info = (ApplicationInfo) getApplication();
				info.setNameList(mynamelist);
				
				if(mynamelist==null){
					Toast.makeText(SettingsActivity.this, "û��ѡ�����ݣ�", Toast.LENGTH_SHORT);
					return;
				}
				
				if(mtype.trim().equals("video")){
					chooseVideo();
				}else if(mtype.trim().equals("image")){
					Intent intent=new Intent();
					//intent.putExtra("namelist", mynamelist);
					intent.setClass(SettingsActivity.this, ImageShowActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
					finish();
				}
				
 
			}
		}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		}).create();
		namelistDialog.show();
	}
	
	private void CreateFolder(){
		
		filePath=ApplicationInfo.getInstance().getFilePath();
		newVideoFileDir=ApplicationInfo.getInstance().getVideoPath();
		newImageFileDir=ApplicationInfo.getInstance().getImagePath();
		
		newAPKFileDir=filePath+File.separator+"apk";
		apkPath=newAPKFileDir+File.separator+"JJL.apk";
 
	}
	
	private void InitFileDownloadListener(){
		mOnFileDownloadStatusListener=new OnSimpleFileDownloadStatusListener(){
			@Override
			public void onFileDownloadStatusWaiting(DownloadFileInfo downloadFileInfo) {
				Log.i("TAG", "onFileDownloadStatusWaiting");
				if(downloadVideoUrl!=null)
				{
					showDownloadVideoProgressBarDialog();
				}else if(downloadImageUrl!=null)
				{	
					if(nLen==0){
						showDownloadImageProgressBarDialog();
					}
				}else if(apkurl!=null)
				{
					showUpdateProgressDialog();
				}
			}
			@Override
			public void onFileDownloadStatusPaused(DownloadFileInfo downloadFileInfo) {
				Log.i("TAG", "onFileDownloadStatusPaused");
			}
			@Override
			public void onFileDownloadStatusPreparing(DownloadFileInfo downloadFileInfo) {
				Log.i("TAG", "onFileDownloadStatusPreparing");
			}
			@Override
			public void onFileDownloadStatusPrepared(DownloadFileInfo downloadFileInfo) {
				Log.i("TAG", "onFileDownloadStatusPrepared");
			}
			@Override
			public void onFileDownloadStatusDownloading(DownloadFileInfo downloadFileInfo, float downloadSpeed,
					long remainingTime) {
				// �������أ�downloadSpeedΪ��ǰ�����ٶȣ���λKB/s��remainingTimeΪԤ����ʣ��ʱ�䣬��λ��
				Log.i("TAG", "onFileDownloadStatusDownloading");
				
				if(remainingTime>-1){ 
					String str=null;
					if(downloadVideoUrl!=null){
						str="�����ٶȣ�"+df.format(downloadSpeed) + " kb/s, ʣ��ʱ�䣺" + remainingTime + " s";
					}else if(downloadImageUrl!=null){
						str="�������ص�"+(nLen+1)+"��ͼƬ, �����ٶȣ�"+df.format(downloadSpeed) + " kb/s, ʣ��ʱ�䣺" + remainingTime + " s";
					}
					progressDialog.setMessage(str);
				}
			}
			@Override
			public void onFileDownloadStatusCompleted(DownloadFileInfo arg0) {
 
				if(downloadVideoUrl!=null)
				{
					newFileDir=newVideoFileDir;
					FileDownloader.move(videoUrl, newFileDir, onMoveDownloadFileListener);
					// ������ɣ������ļ��Ѿ�ȫ��������ɣ�
					Message message=new Message();
					message.what=2;
					myHandler.sendMessage(message);	
				}else if(downloadImageUrl!=null){
					newFileDir=newImageFileDir;
					FileDownloader.move(imageUrl, newFileDir, onMoveDownloadFileListener);
					// ������ɣ������ļ��Ѿ�ȫ��������ɣ�
					Message message=new Message();
					message.what=7;
					myHandler.sendMessage(message);	
 
				}else if(apkurl!=null){
					
					newFileDir=newAPKFileDir;
					FileDownloader.move(apkurl, newFileDir, onMoveDownloadFileListener);
					Message message=new Message();
					message.what=6;
					myHandler.sendMessage(message);	
				}
			}
			@Override
		    public void onFileDownloadStatusFailed(String url, DownloadFileInfo downloadFileInfo, FileDownloadStatusFailReason failReason) {
		        // ����ʧ���ˣ���ϸ�鿴ʧ��ԭ��failReason����Щʧ��ԭ������ܱ������
				
				Message message=new Message();
				message.what=3;
				myHandler.sendMessage(message);	
				
		        String failType = failReason.getType();
		        String failUrl = failReason.getUrl();// ��failUrl = url��url��failReason.getType()����һ����

		        if(FileDownloadStatusFailReason.TYPE_URL_ILLEGAL.equals(failType)){
		            // ����failUrlʱ����url����
		        }else if(FileDownloadStatusFailReason.TYPE_STORAGE_SPACE_IS_FULL.equals(failType)){
		            // ����failUrlʱ���ֱ��ش洢�ռ䲻��
		        }else if(FileDownloadStatusFailReason.TYPE_NETWORK_DENIED.equals(failType)){
		            // ����failUrlʱ�����޷���������
		        }else if(FileDownloadStatusFailReason.TYPE_NETWORK_TIMEOUT.equals(failType)){
		            // ����failUrlʱ�������ӳ�ʱ
		        }else{
		            // �������....
		     }
		     // �鿴��ϸ�쳣��Ϣ
		     Throwable failCause = failReason.getCause();// ��failReason.getOriginalCause()
		     // �鿴�쳣������Ϣ
		     String failMsg = failReason.getMessage();// ��failReason.getOriginalCause().getMessage()
		    }
		};
		FileDownloader.registerDownloadStatusListener(mOnFileDownloadStatusListener);
		
		
		mOnDownloadFileChangeListener=new OnDownloadFileChangeListener(){
			@Override
			public void onDownloadFileCreated(DownloadFileInfo arg0) {
				Log.i("TAG", "onDownloadFileCreated");
			}
			@Override
			public void onDownloadFileDeleted(DownloadFileInfo arg0) {				
				Log.i("TAG", "onDownloadFileDeleted");
			}
			@Override
			public void onDownloadFileUpdated(DownloadFileInfo arg0, Type arg1) {				
				Log.i("TAG", "onDownloadFileUpdated");
			}
		};
		FileDownloader.registerDownloadFileChangeListener(mOnDownloadFileChangeListener);

		onMoveDownloadFileListener=new OnMoveDownloadFileListener(){
			@Override
			public void onMoveDownloadFileFailed(DownloadFileInfo arg0, MoveDownloadFileFailReason arg1) {
				// TODO Auto-generated method stub
				Log.d("TAG", "Move Failed!");
			}
			@Override
			public void onMoveDownloadFilePrepared(DownloadFileInfo arg0) {
				// TODO Auto-generated method stub
				Log.d("TAG", "Move Prepared!");
			}
			@Override
			public void onMoveDownloadFileSuccess(DownloadFileInfo arg0) {
				// TODO Auto-generated method stub
				Log.d("TAG", "Move Success!");
			}
		};

		
		videoOnDeleteDownloadFileListener=new OnDeleteDownloadFileListener(){
			@Override
			public void onDeleteDownloadFileFailed(DownloadFileInfo arg0, DeleteDownloadFileFailReason arg1) {
				Log.i("TAG", "onDeleteDownloadFileFailed");
				Message msg=new Message();
				Bundle b=new Bundle();
				b.putString("url", downloadVideoUrl);
				msg.what=4;
				msg.setData(b);
				myHandler.sendMessage(msg);
			}
			@Override
			public void onDeleteDownloadFilePrepared(DownloadFileInfo arg0) {				
				Log.i("TAG", "onDeleteDownloadFilePrepared");
			}
			@Override
			public void onDeleteDownloadFileSuccess(DownloadFileInfo arg0) {				
				Log.i("TAG", "onDeleteDownloadFileSuccess");				
				Message msg=new Message();
				Bundle b=new Bundle();
				b.putString("url", downloadVideoUrl);
				msg.what=4;
				msg.setData(b);
				myHandler.sendMessage(msg);
			}
		};
		
		imageOnDeleteDownloadFileListener=new OnDeleteDownloadFileListener(){
			@Override
			public void onDeleteDownloadFileFailed(DownloadFileInfo arg0, DeleteDownloadFileFailReason arg1) {
				Log.i("TAG", "onDeleteDownloadFileFailed");
				Message msg=new Message();
				Bundle b=new Bundle();
				b.putString("url", downloadImageUrl);
				msg.what=8;
				msg.setData(b);
				myHandler.sendMessage(msg);
			}
			@Override
			public void onDeleteDownloadFilePrepared(DownloadFileInfo arg0) {				
				Log.i("TAG", "onDeleteDownloadFilePrepared");
			}
			@Override
			public void onDeleteDownloadFileSuccess(DownloadFileInfo arg0) {				
				Log.i("TAG", "onDeleteDownloadFileSuccess");				
				Message msg=new Message();
				Bundle b=new Bundle();
				b.putString("url", downloadImageUrl);
				msg.what=8;
				msg.setData(b);
				myHandler.sendMessage(msg);
			}
		};
		
		updateOnDeleteDownloadFileListener=new OnDeleteDownloadFileListener(){
			@Override
			public void onDeleteDownloadFileFailed(DownloadFileInfo arg0, DeleteDownloadFileFailReason arg1) {
				Message msg=new Message();
				Bundle b=new Bundle();
				b.putString("url", apkurl);
				msg.what=5;
				msg.setData(b);
				myHandler.sendMessage(msg);
			}
			@Override
			public void onDeleteDownloadFilePrepared(DownloadFileInfo arg0) {				
				Log.i("TAG", "onDeleteDownloadFilePrepared");
			}
			@Override
			public void onDeleteDownloadFileSuccess(DownloadFileInfo arg0) {								
				Message msg=new Message();
				Bundle b=new Bundle();
				b.putString("url", apkurl);
				msg.what=5;
				msg.setData(b);
				myHandler.sendMessage(msg);
			}
		};
 
	}
	private void chooseVideo(){
		Intent intent=new Intent();
		intent.setType("video/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent,1);
		
	}
	
	protected void onActivityResult(int requestCode,int resultCode,Intent data){
		if(requestCode==1){
			if(resultCode==RESULT_OK){
				Uri uri = data.getData();  
				//String mypath=UriUtils.getPath(SettingsActivity.this, uri);
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);  
                cursor.moveToFirst();  
                //v_path = cursor.getString(1); 
                v_path=UriUtils.getPath(SettingsActivity.this, uri);
                v_name = cursor.getString(2);  
                v_size = cursor.getString(3);  
                progress=0;
                showUploadProgressBarDialog();
                List<Param> list = new ArrayList<OkHttpUtils.Param>();
                if(mynamelist!=null){
                	list.add(new Param("namelist",mynamelist));
                }else{
                	list.add(new Param("namelist","null"));
                }
                
                uploadFile(APPConfig.uploadFile,v_path,v_name,list);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
//======================================
	private void uploadFile(String url,String vpath,String vname,List<Param> params){
    	MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
    	
    	for(Param param:params){
    		builder.addPart(Headers.of("Content-Disposition","form-data; name=\"" + param.key + "\""),
    				RequestBody.create(null, param.value));
    	}
    	
        File file = new File(vpath);        
        builder.addPart(Headers.of("Content-Disposition",
				"form-data; name=\"video\"; filename=\"" + vname + "\""),createCustomRequestBody(MediaType.parse(guessMimeType(file.getName())), file, new ProgressListener() {
		            @Override 
		            public void onProgress(long totalBytes, long remainingBytes, boolean done) {
		                
		            }
		        }));
 
    	RequestBody requestBody = builder.build(); 
    	Request request = new Request.Builder()
							.url(url)
							.post(requestBody)
							.build();
    	
    	okHttpClient.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Request request, IOException e) {
            	Log.i("TAG_Failure", "Failure");
            }

            @Override public void onResponse(Response response) throws IOException {
            	//Log.i("TAG_Success", "response.body().string() = " + response.body().string());
            	progressDialog.dismiss();
            	Message message=new Message();
				message.what=1;
				myHandler.sendMessage(message);	
            	
            }
        });
	}
	private String guessMimeType(String path){
    	FileNameMap fileNameMap=URLConnection.getFileNameMap();
    	String contentTypeFor=fileNameMap.getContentTypeFor(path);
    	if(contentTypeFor==null){
    		contentTypeFor="application/octet-stream";
    	}
    	return contentTypeFor;
    }
	private static RequestBody createCustomRequestBody(final MediaType contentType, final File file, final ProgressListener listener) {
        return new RequestBody() {
            @Override 
            public MediaType contentType() {
                return contentType;
            }

            @Override 
            public long contentLength() {
                return file.length();
            }

            @Override 
            public void writeTo(BufferedSink sink) throws IOException {
                Source source;
                try {
                    source = Okio.source(file);
                    Buffer buf = new Buffer();
                    Long remaining = contentLength();
                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                        sink.write(buf, readCount);
                        listener.onProgress(contentLength(), remaining -= readCount, remaining == 0);
                        
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
    interface ProgressListener {
        void onProgress(long totalBytes, long remainingBytes, boolean done);
    }
 //======================================
    
    private void showDownloadVideoProgressBarDialog(){
    	progressDialog.setMessage("׼��������Ƶ�����Ե�...");
    	progressDialog.setCanceledOnTouchOutside(false);
    	progressDialog.show();
    }
    
    private void showDownloadImageProgressBarDialog(){
    	progressDialog.setMessage("׼������ͼƬ�����Ե�...");
    	progressDialog.setCanceledOnTouchOutside(false);
    	progressDialog.show();
    }
    
	private void showUploadProgressBarDialog(){	
    	progressDialog.setMessage("�����ϴ�,���Ե�...");
    	progressDialog.setCanceledOnTouchOutside(false);
    	progressDialog.show();
	}
	
	private void showUpdateProgressDialog(){
		progressDialog.setMessage("�����������°汾,���Ե�...");
    	progressDialog.setCanceledOnTouchOutside(false);
    	progressDialog.show();
	}
	@Override
	protected void onDestroy() {//Activity���ݻ�ʱ������
		super.onDestroy();
		FileDownloader.unregisterDownloadStatusListener(mOnFileDownloadStatusListener);
		FileDownloader.unregisterDownloadFileChangeListener(mOnDownloadFileChangeListener);
	}
	
	public boolean onKeyDown(int keyCode,KeyEvent event){
		if(keyCode==KeyEvent.KEYCODE_BACK){
			Intent intent=new Intent();
			intent.setClass(mContext, MainActivity.class);
			startActivity(intent);					
			overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);						
			SettingsActivity.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
