package com.xy.jjl.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.xy.jjl.R;
import com.xy.jjl.R.id;
import com.xy.jjl.R.layout;
import com.xy.jjl.application.ApplicationInfo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.app.ActionBar.LayoutParams;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageActivity extends Activity {
	
		private ArrayList<String> imagePath=new ArrayList<String>();
		AdapterViewFlipper flipper;
		
		public void getBitmap(){
			
			try{
				File file=null;
				file=Environment.getExternalStorageDirectory();
				String path=file.getAbsolutePath()+File.separator+"TMSA";
				file=new File(path);
				if(file.isDirectory()){					
					for(File f:file.listFiles()){
						String mpath=f.getAbsolutePath();
						if(mpath.endsWith(".jpg")){
							imagePath.add(mpath);
						}
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			//Full screen
			/*requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
								 WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
			setContentView(R.layout.image_show);
			ApplicationInfo.getInstance().addActivity(this);
			
			getBitmap();
			
			flipper=(AdapterViewFlipper)findViewById(R.id.flipper);
			
			BaseAdapter adapter=new BaseAdapter(){
				@Override
				public int getCount(){
					return imagePath.size();
				}
				
				@Override
				public Object getItem(int position){
					return position;
				}
				public long getItemId(int position){
					return position;
				}
				
				public View getView(int position,View convertView,ViewGroup parent){
					ImageView imageView=new ImageView(ImageActivity.this);
					//imageView.setImageResource(imageIds[position]);
					String path=imagePath.get(position);
					Bitmap bitmap=BitmapFactory.decodeFile(path);
					imageView.setImageBitmap(bitmap);
					imageView.setScaleType(imageView.getScaleType().FIT_XY);
					imageView.setLayoutParams(new LayoutParams(
							LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT
							));
					return imageView;
				}
				
			};
			
			flipper.setAdapter(adapter);
			
			flipper.startFlipping();
			
		} 	
		
		public void auto(View source){
			flipper.startFlipping();
		}
		
		public void stop(View source){
			flipper.stopFlipping();
		}
		
		public boolean onKeyDown(int keyCode,KeyEvent event){
			if(keyCode==KeyEvent.KEYCODE_BACK){
				ImageActivity.this.finish();
				return true;
			}
			return super.onKeyDown(keyCode, event);
		}
}
