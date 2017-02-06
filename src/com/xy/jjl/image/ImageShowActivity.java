package com.xy.jjl.image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.xy.jjl.R;
import com.xy.jjl.activity.ClientNetThread;
import com.xy.jjl.activity.MainActivity;
import com.xy.jjl.activity.SettingsActivity;
import com.xy.jjl.activity.testActivity;
import com.xy.jjl.application.ApplicationInfo;
import com.xy.jjl.common.APPConfig;
import com.xy.jjl.utils.Bimp;
import com.xy.jjl.utils.FileUtils;
import com.xy.jjl.utils.ImageItem;
import com.xy.jjl.utils.OkHttpUtils;
import com.xy.jjl.utils.PublicWay;
import com.xy.jjl.utils.Res;
import com.xy.jjl.utils.OkHttpUtils.Param;
import com.xy.jjl.utils.OkHttpUtils.ResultCallback;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;


public class ImageShowActivity extends Activity implements OnClickListener{
	
	private TextView activity_selectimg_send;
	private ImageButton uploadimgeback;
	private GridView noScrollgridview;
	private GridAdapter adapter;
	private View parentView;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	private String myUTFdata;
	public static Bitmap bimap ;
	ClientNetThread clientNetThread = null;//网络处理线程
	private Context context;
	private String u_name;
	private int uid;
	ProgressDialog myDialog;
	
	public final static byte[] PICTURE_PACKAGE_HEAD={
			(byte)0xff,
			(byte)0xcf,
			(byte)0xfc,
			(byte)0xbf,
			(byte)0xfb,
			(byte)0xaf,
			(byte)0xfa,
			(byte)0xff
	};
	
	public final static byte[] PICTURE_PACKAGE_END={
			(byte)0x11,
			(byte)0x11,
			(byte)0x11,
			(byte)0xff
	};
 
	Handler myHandler=new Handler(){
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			switch(msg.what){	
			case 0:
				//提示
				//====================					
				new AlertDialog.Builder(ImageShowActivity.this).setTitle("Send").setMessage("上传失败!")
				.setPositiveButton("OK",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				}).show();	
				System.out.println("ending");
				//====================
				break;	
			case 1:
				//提示
				//====================					
				new AlertDialog.Builder(ImageShowActivity.this).setTitle("Send").setMessage("上传成功!")
				.setPositiveButton("OK",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//返回主界面
						Intent intent=new Intent();
						intent.setClass(ImageShowActivity.this, MainActivity.class);
						startActivity(intent);					
						overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);						
						ImageShowActivity.this.finish();
						return;
					}
				}).show();	
				System.out.println("ending");
				//====================
				break;	
			case 2:
				//提示
				//====================					
				new AlertDialog.Builder(ImageShowActivity.this).setTitle("Send").setMessage("图片已经存在!")
				.setPositiveButton("OK",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				}).show();	
				System.out.println("ending");
				//====================
				break;	
			case 3:
				//发送完成提示
				//====================					
				new AlertDialog.Builder(ImageShowActivity.this).setTitle("Send").setMessage("上传失败!")
				.setPositiveButton("OK",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				}).show();	
				System.out.println("ending");
				//====================
				break;	
			}
		}
	};
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Full screen
		/*requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
							 WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
 
		Res.init(this);
		bimap = BitmapFactory.decodeResource(getResources(),R.drawable.icon_addpic_unfocused);
		
		PublicWay.activityList.add(this);
		ApplicationInfo.getInstance().addActivity(this);
		
		parentView = getLayoutInflater().inflate(R.layout.activity_selectimg, null);
		setContentView(parentView);
		Init();
	}

	@Override
	protected void onDestroy() {//Activity被摧毁时被调用
		super.onDestroy();
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.activity_selectimg_send:
			//######################################################
			if (Bimp.tempSelectBitmap.size()==0){
				new AlertDialog.Builder(ImageShowActivity.this).setTitle("Send").setMessage("请选择图片!")
				.setPositiveButton("OK",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				}).show();					
			}
			else{
				myDialog=ProgressDialog.show(this, "进度", "正在上传...");
				
				//==========================
				File[] files=new File[Bimp.tempSelectBitmap.size()];					
				String[] fileKeys=new String[Bimp.tempSelectBitmap.size()];
				
				List<Param> params = new ArrayList<OkHttpUtils.Param>();
				params.add(new Param("username", "star"));
				params.add(new Param("password", "star"));
				try{
					for(int i=0;i<Bimp.tempSelectBitmap.size();i++){
						
						files[i]= new File(Bimp.tempSelectBitmap.get(i).imagePath);								
						fileKeys[i]="image";//Bimp.tempSelectBitmap.get(i).imagePath;
							
						}
					}catch(Exception e){
						e.printStackTrace();
					}
 
					OkHttpUtils.uploadImage(APPConfig.uploadImage, new ResultCallback<String>(){

						@Override
						public void onSuccess(String response) {
							// TODO Auto-generated method stub
						Log.i("JJL", response);
						myDialog.dismiss();	
						
						JSONObject object;
						String flag;
						
						try{
							object=new JSONObject(response);
							flag=object.getString("flag");
							if(flag.equals("0")){
								Message message=new Message();
								message.what=0;
								myHandler.sendMessage(message);									
							}
							else if(flag.equals("1")){															
								Message message=new Message();
								message.what=1;
								myHandler.sendMessage(message);
							}
							else if(flag.equals("2")){
								Message message=new Message();
								message.what=2;
								myHandler.sendMessage(message);									
							}
						}catch(Exception e){
							e.printStackTrace();
						}
						
					}

					@Override
					public void onFailure(Exception e) {
						// TODO Auto-generated method stub
						Log.i("JJL", "Failure");
						Message message=new Message();
						message.what=3;
						myHandler.sendMessage(message);	
					}
					
				}, files, fileKeys, params);
				
				//==========================
			}
			//######################################################
			break;
			
		case R.id.uploadimgeback:
			for(int i=0;i<PublicWay.activityList.size();i++){
				if (null != PublicWay.activityList.get(i)) {
					PublicWay.activityList.get(i).finish();
				}
			}			
			
			Bimp.tempSelectBitmap.clear();
			
			//---------
			Intent intent=new Intent();
			intent.setClass(ImageShowActivity.this, SettingsActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);	
			//---------
			ImageShowActivity.this.finish();
			break;
			
		default:
			break;
		}
	}
	public void Init() {
		try{

		/*Test Send*/
		activity_selectimg_send=(TextView)findViewById(R.id.activity_selectimg_send);
		activity_selectimg_send.setOnClickListener(this);
		/*---------------------*/
		
		//========
		uploadimgeback=(ImageButton) findViewById(R.id.uploadimgeback);
		uploadimgeback.setOnClickListener(this);
		//=======
		pop = new PopupWindow(ImageShowActivity.this);
		
		View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);

		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		
		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);
		
		RelativeLayout parent = (RelativeLayout)view.findViewById(R.id.parent);
		Button bt1 = (Button)view.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button)view.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button)view.findViewById(R.id.item_popupwindows_cancel);
		
		parent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				photo();
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ImageShowActivity.this,AlbumActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		
		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);	
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				if (arg2 == Bimp.tempSelectBitmap.size()) {
					Log.i("ddddddd", "----------");
					ll_popup.startAnimation(AnimationUtils.loadAnimation(ImageShowActivity.this,R.anim.activity_translate_in));
					pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
				} else {
					Intent intent = new Intent(ImageShowActivity.this,GalleryActivity.class);
					intent.putExtra("position", "1");
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});
		
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			if(Bimp.tempSelectBitmap.size() == 9){
				return 9;
			}
			return (Bimp.tempSelectBitmap.size() + 1);
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_published_grida,parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position ==Bimp.tempSelectBitmap.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));
				if (position == 9) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.tempSelectBitmap.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							Bimp.max += 1;
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
						}
					}
				}
			}).start();
		}
	}//End class

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	protected void onRestart() {
		adapter.update();
		super.onRestart();
	}

	private static final int TAKE_PICTURE = 0x000001;

	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {
				
				String fileName = String.valueOf(System.currentTimeMillis());
				Bitmap bm = (Bitmap) data.getExtras().get("data");
				FileUtils.saveBitmap(bm, fileName);
				
				ImageItem takePhoto = new ImageItem();
				takePhoto.setBitmap(bm);
				Bimp.tempSelectBitmap.add(takePhoto);
			}
			break;
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			for(int i=0;i<PublicWay.activityList.size();i++){
				if (null != PublicWay.activityList.get(i)) {
					PublicWay.activityList.get(i).finish();
				}
			}			
			
			Bimp.tempSelectBitmap.clear();
			
			//---------
			Intent intent=new Intent();
			intent.setClass(ImageShowActivity.this, SettingsActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);	
			//---------
			ImageShowActivity.this.finish();
		}
		return true;
	}

	
}
