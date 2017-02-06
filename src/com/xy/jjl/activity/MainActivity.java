package com.xy.jjl.activity;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.utils.Log;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wlf.filedownloader.FileDownloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.xy.jjl.R;
import com.xy.jjl.adapter.ViewpagerDotsAdapter;
import com.xy.jjl.application.ApplicationInfo;
import com.xy.jjl.common.APPConfig;
import com.xy.jjl.utils.CashBoxCheck;
import com.xy.jjl.utils.MoneyUnit;
import com.xy.jjl.utils.OkHttpUtils;
import com.xy.jjl.utils.OkHttpUtils.Param;
import com.xy.jjl.utils.OkHttpUtils.ResultCallback;
import com.xy.jjl.utils.PrinterUnit;
import com.xy.jjl.view.VitomioVideo;
					  
public class MainActivity extends Activity implements OnClickListener {
	//装载动态轮播图
	private List<View> dots = new ArrayList<View>();
	private List<ImageView> list_img = new ArrayList<ImageView>();
	//当前轮播图
	private int currentItem = 0;
	private boolean isShowImage;
	
	//当前播放媒体 0：图片   1：视频   2：图片视频轮流播放
	private int currentMedia=0;
	
	private ImageView iv_main_setting;
	private LinearLayout ll_main;
	private LinearLayout ll_main_video;
	private VitomioVideo vv_view;
	
	private TextView tv_time;
	private TextView tv_date;
	
	private FrameLayout fl_view;
 
	private ViewPager vp;
	private LinearLayout ll_dots;
	//模拟轮播图链接
	private List<String> pic_link =null;
	
	//视频连接
	private List<String> video_link=new ArrayList<String>();
	

	private ScheduledExecutorService scheduledExecutorService;
	
	private LayoutInflater inflater;
	//格式化当前时间
	SimpleDateFormat format = new SimpleDateFormat("HH:mm");
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年M月d日");
	//记录设置
	private int setting ;
	
	private Context TAG = MainActivity.this;
	private int beginFlag=0;
	
	//--------------
	private Context mContext=MainActivity.this;
	private Dialog dialog;
	private String name=null;
	private String path=null;
	private String videoname=null;;
	private String videopath=null;
	private String imagename=null;
	private String imagepath =null;
	private String flag=null;
	//--------------
	//=============================
			 
			// 序号
			String OrderNumber;
			// 机台编号
			String MachineNumber = "设备号:JJL00001";
		
			String TimeData; // 加密用时间数据，从系统时间获取
			String QrCodeData; // 用于生成二维码的数据
			String QrCodeDataPath; // 二维码保存路径（/data/data/应用包名/cache）
			String MD5DataSource; // MD5加密的中间值
			String MD5Data; // 用于制作二维码的最终MD5加密数据
			String MD5Key = "LDkeyNwfFC3sKcBIlBh917nwlUHjGfG5"; // MD5加密密钥
		
			int TIME = 500;
		
			int Money; // 金额
			
			int CashBoxStatusFlag;
		
			int OrderTestNumber = 1; // 顺序号（测试用）
			int TestMoney = 5; // 金额（测试用）
		
			FileInputStream fis = null;
		
			// 获得收银机工具类的对象
			MoneyUnit mMoneyUnit = new MoneyUnit();
		
			// 获得打印机工具类的对象
			PrinterUnit mPrinterUnit = new PrinterUnit();
			
			// 获得钱箱状态检测工具类的对象
			CashBoxCheck mCashBoxCheck = new CashBoxCheck();
		
			Handler moneyReceiveHandler = new Handler();
			Runnable moneyReceiveRunnable = new Runnable() {
		
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Money = mMoneyUnit.rcvdMoney;
						// OrderNumber = OrderDispose(OrderTestNumber);
						//Money = 10;
						if (Money > 0) {
							switch (Money) {
							case 5:
								qrCodeConfig(Money);
								mPrinterUnit.PrintTicket(Money, OrderNumber, fis);
								mMoneyUnit.rcvdMoney = -1;
								Money = -1;
								break;
							case 10:
								Log.i("TAG", "Here!");
								qrCodeConfig(Money);
								//delay(6000);
								
								try   
								{   
								Thread.currentThread().sleep(10000);//毫秒   
								}   
								catch(Exception e){}  
								
								Log.i("TAG", "Sleep DONE! Print ticket!!");
								
								mPrinterUnit.PrintTicket(Money, OrderNumber, fis);
								OrderTestNumber++;
								mMoneyUnit.rcvdMoney = -1;
								Money = -1;
								break;
							case 20:
								Money = 15;
								qrCodeConfig(Money);
								//delay(6000);
								
								try   
								{   
								Thread.currentThread().sleep(10000);//毫秒   
								}   
								catch(Exception e){}
								
								Log.i("TAG", "Sleep DONE! Print ticket!!");
								
								mPrinterUnit.PrintTicket(Money, OrderNumber, fis);
								
								//print 余额
								//--------------
								Log.i("TAG", "Sleep DONE! Print ticket,5RMB!!");
								Money = 5;
								qrCodeConfig(Money);
								mPrinterUnit.PrintTicket(Money, OrderNumber, fis);
								//--------------
								OrderTestNumber++;
								mMoneyUnit.rcvdMoney = -1;
								Money = -1;
								break;
							}
						} else {
							mMoneyUnit.rcvdMoney = -1;
							Money = -1;
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
		
					moneyReceiveHandler.postDelayed(this, TIME);
				}
			};
			
			Handler CashBoxCheckHandler = new Handler();
			Runnable CashBoxCheckRunnable = new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						CashBoxStatusFlag = mCashBoxCheck.CashBoxStatusGet();
						
						if(CashBoxStatusFlag == 1) {
							Log.i("TAG", "CashBox is opened!!!!!!");
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
					
					CashBoxCheckHandler.postDelayed(this, 300);
				}
			};


			private void qrCodeConfig(int money) { 
				// 获取当前系统时间，保存在TimeData里
				SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
				TimeData = sDateFormat.format(new java.util.Date());

				// 处理顺序号（从整型1处理为String型001）
				OrderNumber = OrderDispose(OrderTestNumber);
				//Log.i(TAG, "OrderNumber = " + OrderNumber);

				// 处理金额（从整型变为String）
				String MoneyS = String.valueOf(money);
				// 组合需要加密的数据
				MD5DataSource = MachineNumber + MoneyS + TimeData + MD5Key;
				//Log.i(TAG, "MD5TestData = " + MD5DataSource);

				// 对MD5原始数据进行加密
				char[] MD5DataGet = mPrinterUnit.Md5encrypt(MD5DataSource);
				
				// 获取从第11个字节开始的8个字节
				char[] MD5dataCut = new char[8];
				for (int i = 0; i < 8; i++) {
					MD5dataCut[i] = MD5DataGet[10 + i];
				}

				// 将这8个字节转换为String型
				MD5Data = new String(MD5dataCut);
				//Log.i(TAG, "MD5Data = " + MD5Data);

				// 组合制作二维码的全部数据
				QrCodeData = "http://wx.uuudian.com/device/LDPoint.do?device="
						+ MachineNumber + "&time=" + TimeData + "&ticket=" + TestMoney
						+ "&sign=" + MD5Data;
				//Log.i(TAG, "QrCodeData = " + QrCodeData);

				// 获取保存二维码图片的路径
				QrCodeDataPath = getDiskCacheDir(this) + File.separator + "QrImg.bmp";

				// 调用打印机工具类，将数据传入制作二维码
				mPrinterUnit.qrCodeCreate(QrCodeData, QrCodeDataPath);

				// 制作完毕后获取二维码图片的流数据，转换成打印用的字节
				try {
					fis = new FileInputStream(new File(QrCodeDataPath));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// 获取二维码保存路径（/data/data/应用包名/cache）
			private String getDiskCacheDir(Context context) {
				String cachePath = null;
				cachePath = context.getCacheDir().getPath();

				return cachePath;
			}

			// 处理顺序号，只支持100以内
			private String OrderDispose(int OrderData) {

				String reOrder = null;

				if (OrderData < 10) {
					String s = String.valueOf(OrderData);
					reOrder = "0" + "0" + s;

				}

				if ((OrderData >= 10) && (OrderData < 100)) {
					String s = String.valueOf(OrderData);
					reOrder = "0" + s;
				}
				
				return reOrder;
			}

			// 延时函数
			private void delay(int ms) {
				try {
					Thread.currentThread();
					Thread.sleep(ms);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			// 将byte[]转换为String
			public static String bytes2HexString(byte[] b) {
				StringBuffer result = new StringBuffer();
				String hex;
				for (int i = 0; i < b.length; i++) {
					hex = Integer.toHexString(b[i] & 0xFF);
					if (hex.length() == 1) {
						hex = '0' + hex;
					}
					result.append(hex.toUpperCase());
				}
				return result.toString();
			}
			//===========================
   
	private Handler handler = new Handler(){
		@SuppressLint("HandlerLeak")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				//切换轮播图，并且更新时间
				vp.setCurrentItem(currentItem);				
				tv_time.setText(format.format(new Date()));
				tv_date.setText(dateFormat.format(new Date()));
				//---------
				int flag= msg.arg1;
				if(flag==1){
					isShowImage=false;					
					MediaSetting(1);//视频	
					Log.d("Video start", "Video start");
				}
				//---------
				break;
			case 1:
				initData();
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
		setContentView(R.layout.activity_main);
		
		ApplicationInfo.getInstance().addActivity(this);
		
		initMachine();
		
		initView();
		
		initMedia();
		
		/*String str1=ApplicationInfo.getInstance().getVideoPath();
		Log.d("test", str1);*/
		
	}
	
	public void initMachine(){
   
		mMoneyUnit.billAcceptorCmdInit();
		mMoneyUnit.billAcceptorDeviceInit();
		mMoneyUnit.billAcceptorConnecting();
		mMoneyUnit.billAcceptorDataGet();
		
		//mCashBoxCheck.CashBoxDeviceInit();

		// 初始化机台编号
		mPrinterUnit.PrinterInit(MachineNumber);

		//循环接收打印金额，一旦接收到金额就开始打印
		moneyReceiveHandler.postDelayed(moneyReceiveRunnable, TIME);
		
	}
	
	/**
	 * 初始化界面
	 */
	public void initView(){
		vp = (ViewPager) findViewById(R.id.vp);
		ll_dots = (LinearLayout) findViewById(R.id.ll_dots);
		iv_main_setting = (ImageView) findViewById(R.id.iv_main_setting);
		ll_main = (LinearLayout) findViewById(R.id.ll_main);
		ll_main_video=(LinearLayout) findViewById(R.id.ll_main_video);
		vv_view = (VitomioVideo) findViewById(R.id.vv_view);
		fl_view = (FrameLayout) findViewById(R.id.fl_view);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_date = (TextView) findViewById(R.id.tv_date);
		inflater = LayoutInflater.from(this);
		
		tv_time.setText(format.format(new Date()));
		tv_date.setText(dateFormat.format(new Date()));
		
		//监听界面点击事件
		iv_main_setting.setOnClickListener(this);
		ll_main.setOnClickListener(this);
	}
	
	
	
	//初始化模拟数据
	public void initData(){
		//取出设置，0为图片，1为视频
		setting = 2;// RsSharedUtil.getInt(TAG, APPConfig.SETTING);
		//setting=1;
		seletMedia(setting);//设置播放媒体
		//获取banner,设置轮播图
		getBanner();
	}
	
	private void initMedia(){
		pic_link= new ArrayList<String>();
		String tempstr=ApplicationInfo.getInstance().getImagePath();
		File files=new File(tempstr);
		if(files.exists() && files.isDirectory())
		{
			if(files.length()>0){
				String name[]=files.list();
				for(int i=0;i<name.length;++i){
					File f=new File(tempstr,name[i]);
					pic_link.add(f.getPath());
				}
			}
			
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
		}
		
		if(pic_link==null){
			getMediaPath();
		}
		//getVideoPath();
	}
	
	private void getVideoPath(){
		new Thread(){
			public void run() {
				//--------------
				//获取视频信息
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
							flag=obj.getString("flag");//0: 表示不存在用户  1:表示获取用户列表成功
							if(flag.equals("1")){
								name=obj.getString("videoname");
								path=obj.getString("videopath");										
							}
						}catch(JSONException e){
							e.printStackTrace();
							flag="0";
						}
						if(flag.equals("0")){
							//InitDialog("提示","没有获取到视频信息！");								
							return;
						}else{
							videopath=APPConfig.BaseLink + path + name;
							path=null;
							name=null;
							setVideo();
						}
					};
					public void onFailure(Exception e) {
						//InitDialog("提示","没有获取到视频信息！");
						return;
					};
				}, list);
			}
		}.start();
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
		dialog.show();
	}
	public void getMediaPath(){
 
		new Thread(){
			public void run() {
				List<Param> list = new ArrayList<OkHttpUtils.Param>();
				list.add(new Param("user_name", "star"));
				list.add(new Param("password", "star"));
				OkHttpUtils.post(APPConfig.Media, new ResultCallback<String>() {
					public void onSuccess(String response) {
						Log.d("JJL", "OKhttp Success!");
						
						try{
							
							JSONArray array=new JSONArray(response);
							JSONObject json;
							JSONObject jsonObj;
							String key=null;
							String value=null;
							for(int i=0;i<array.length();++i){
								json=array.getJSONObject(i);
								Iterator it=json.keys();
								while(it.hasNext()){
									key=it.next().toString();
									value=json.getString(key);							
								}
								if(key.equals("image")){
									if(value!=null){
										jsonObj=new JSONObject(value);
										it=jsonObj.keys();
										while(it.hasNext()){
											key=it.next().toString();
											value=jsonObj.getString(key);	
											pic_link.add(APPConfig.BaseLink+value);
										}
									}
								}else if(key.equals("video")){
									if(value!=null){
										jsonObj=new JSONObject(value);
										it=jsonObj.keys();
										while(it.hasNext()){
											key=it.next().toString();
											value=jsonObj.getString(key);	
											video_link.add(APPConfig.BaseLink+value);
										}
									}
								}
								
							}
							
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							
						}catch(JSONException e){
							e.printStackTrace();
						}

					};
					public void onFailure(Exception e) {
						Log.d("JJL", "OKhttp Failure!");
						
					};
				}, list);
			};
		}.start();
	}
	
	public void seletMedia(int setting){
		
		switch(setting){
		case 0:
			currentItem=0;
			currentMedia=0;//图片
			MediaSetting(0);
			break;
		case 1:
			currentMedia=1;//视频
			MediaSetting(1);
			Log.d("Video start", "Video start");
			break;
		case 2:
			currentMedia=2;//图片视频轮流播放
			currentItem=0;
			isShowImage=true;
			MediaSetting(0);//先播放图片
			Log.d("image start", "image start");
			break;
		}
	}
	//根据设置隐藏界面
	public void MediaSetting(int flag){
		switch(flag){		
		case 0://图片
			vv_view.setVisibility(View.GONE);
			fl_view.setVisibility(View.VISIBLE);
			break;
		case 1://视频
			vv_view.setVisibility(View.VISIBLE);
			fl_view.setVisibility(View.GONE);
			//setVideo();
			getVideoPath();
			break;
		}
	}
 
	public void setVideo(){
		//如果vedio可见，播放视频,播放一个视频
		if (vv_view.getVisibility()==View.VISIBLE) {
			//path="http://112.74.38.240:8080/Video/VTest_02.mp4";
			path=videopath;//服务器视频
			path=null;
			
			
			
			String tempstr=ApplicationInfo.getInstance().getVideoPath();
			File files=new File(tempstr);
			if(files.exists() && files.isDirectory())
			{
				if(files.length()>0){
					String name[]=files.list();//暂时只有一个视频文件
					for(int i=0;i<name.length;++i){
						File f=new File(tempstr,name[i]);
						path=f.getPath();
					}
				}
			}
			
			Log.d("video path:" + path, path);
			
			if(path==null){
				Log.d("video path  null --> return", " null --> return");
				return;
			}
			
			vv_view.setVideoPath(path);
			vv_view.setVideoLayout(VideoView.VIDEO_LAYOUT_STRETCH, 0);
			//获取view的参数
			final DisplayMetrics metrics = vv_view.getResources().getDisplayMetrics();
			vv_view.setOnPreparedListener(new OnPreparedListener() {
				
				@Override
				public void onPrepared(MediaPlayer mp) {
					// TODO Auto-generated method stub
					mp.setPlaybackSpeed(1.0f);
					//获取视频资源的宽高
					int videoWidth = mp.getVideoWidth();
					int videoHeight = mp.getVideoHeight();
					//算出最终的宽高
					videoWidth = metrics.widthPixels;
					videoHeight=ll_main_video.getHeight();					
					//重新设置宽高
					vv_view.getHolder().setFixedSize(videoWidth, videoHeight);
					vv_view.setMeasure(videoWidth, videoHeight);
					vv_view.requestLayout();
					mp.start();
				}
			});
			vv_view.setMediaController(new MediaController(this));
			vv_view.requestFocus();
			vv_view.setOnCompletionListener(new OnCompletionListener(){

				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					
					if(currentMedia==2){
						Log.d("video change to image", "video change to image");
						currentItem=0;
						isShowImage=true;
						MediaSetting(0);//图片
					}else if(currentMedia==1){
						Log.d("video end", "Video end");
						//重复播放视频
						vv_view.setVideoPath(path);
						vv_view.start();
						mp.start();
						Log.d("video repeat start", "video repeat start");
					}
				}
				
			});
			vv_view.setVideoLayout(VideoView.VIDEO_LAYOUT_STRETCH, 0);
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.ll_main:
			//触摸屏幕会跳转到订票界面
			TAG.startActivity(new Intent(TAG,SelectActivity.class));
			((Activity) TAG).finish();
			break;
		case R.id.iv_main_setting:
			//点击设置按钮事件，这里进行设置的操作
			//Toast.makeText(TAG, "setting", Toast.LENGTH_LONG).show();
			TAG.startActivity(new Intent(TAG,SettingsActivity.class));
			((Activity) TAG).finish();
			break;
		default:
			break;
		}
	}
	
	public void getBanner(){
		//动态设置轮播图数量
		list_img.clear();
		for (int i = 0; i < pic_link.size(); i++) {
			ImageView iv = new ImageView(MainActivity.this);
			iv.setScaleType(ScaleType.FIT_XY);
			if(pic_link.get(i).startsWith("http")){
				Picasso.with(MainActivity.this).load(pic_link.get(i)).into(iv);
			}else{
				File f=new File(pic_link.get(i));
				Picasso.with(MainActivity.this).load(f).into(iv);
			}
			list_img.add(iv);
			View dot = inflater.inflate(R.layout.item_dots, null);
			dots.add(dot);
			ll_dots.addView(dot);
		}
		//设置轮播图
		vp.setAdapter(new ViewpagerDotsAdapter(MainActivity.this, list_img));
		vp.setOnPageChangeListener(new MyPageChangeListener());
	}
	
	/**
	 * 换行切换任务
	 * 
	 * @author Administrator
	 * 
	 */
	private class ScrollTask implements Runnable {

		public void run() {
			synchronized (vp) {
				currentItem = (currentItem + 1) % list_img.size();				
				Message message = new Message();
				//=================
				if(currentMedia==2 && currentItem==0 && isShowImage==true){
					message.arg1=1;
				}else{
					message.arg1=0;
				}
				//=================
				
				message.what = 0;
				handler.sendMessage(message);
			}
		}
	}
	//开启时执行延迟服务
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleWithFixedDelay(new ScrollTask(), 1, 5, TimeUnit.SECONDS);
	}
	
	//关掉延迟服务
	@Override
	public void onDestroy(){
		// TODO Auto-generated method stub
		super.onDestroy();
		scheduledExecutorService.shutdown();
	}
	
	private class MyPageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public boolean onKeyDown(int keyCode,KeyEvent event){
		new AlertDialog.Builder(TAG).
		setTitle("提示").
		setMessage("是否退出系统!").
		setPositiveButton("Yes",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				scheduledExecutorService.shutdown();
				
				ApplicationInfo.getInstance().exit();
 
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
