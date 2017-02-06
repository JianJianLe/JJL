package com.xy.jjl.view;

import com.xy.jjl.activity.testActivity;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;

public class MainView extends ImageView {
	private Context mcontext;
	private double w;
	private double h;
	//--------------
	//文字绘制
	private String mNumberText;
	private int mNumberTextColor;
	private int mNumberTextSize;
	private String mText;
	private int mTextColor;
	private int mTextSize;
	
	private Rect mBound;
	private Paint mPaintNumber;
	private Paint mPaintText;
	
	private float numberX;
	private float numberY;
	private float textX;
	private float textY;
	//--------------
	
	//private Bitmap mBackGround;
	
		
	public MainView(Context context){
		this(context,null);
	}
	public MainView(Context context, AttributeSet attrs) {
		super(context, attrs);	
		mcontext=context;
		init();
	}
	
	//不同API进入的位置不同
	public MainView(Context context, AttributeSet attrs,int defStyle){
		super(context,attrs,defStyle);
		mcontext=context;
		init();
	}
	
	private void init(){		
		try{
			
		w=((testActivity) mcontext).getW();	
		h=((testActivity) mcontext).getH();
		//==============
		//绘制文字
			mNumberText="123";
			mNumberTextColor=Color.BLACK;
			mNumberTextSize=16;
			mText="test";
			mTextColor=Color.BLACK;
			mTextSize=16;
		//==============
		
		//设置背景图
		//mBackGround=((BitmapDrawable)this.getResources().getDrawable(R.drawable.tmsa_main_test)).getBitmap();
		
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	//绘制文字
	//==========================
	public void setNumberXY(float x,float y){
		this.numberX=x;
		this.numberY=y;
	}

	public void setNumberText(String mtext){
		this.mNumberText=mtext;
	}
	public void setNumberColor(int mtextcolor){
		this.mNumberTextColor=mtextcolor;
	}
	public void setNumberSize(int mtextsize){
		this.mNumberTextSize=mtextsize;
	}
	public void setNumberPaint(){		
		mPaintNumber=new Paint();
		mPaintNumber.setTextSize(mNumberTextSize);
		mPaintNumber.setColor(mNumberTextColor);
		mBound=new Rect();
		mPaintNumber.getTextBounds(mNumberText, 0, mNumberText.length(), mBound);
	}
	
	//----------------------
	public void setTextXY(float x,float y){
		this.textX=x;
		this.textY=y;
	}

	public void setText(String mtext){
		this.mText=mtext;
	}
	public void setTextColor(int mtextcolor){
		this.mTextColor=mtextcolor;
	}
	public void setTextSize(int mtextsize){
		this.mTextSize=mtextsize;
	}
	public void setTextPaint(){		
		mPaintText=new Paint();
		mPaintText.setTextSize(mTextSize);
		mPaintText.setColor(mTextColor);
		mBound=new Rect();
		mPaintText.getTextBounds(mText, 0, mText.length(), mBound);
	}
	//==========================
	
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		
		//绘制文字
		//---------------
			/*float x;
			float y;
			x=(float)w*(429f/720f);
			y=(float)h*(226f/1280f);
			mX=x;
			mY=y;*/
			canvas.drawText(mNumberText, numberX,numberY, mPaintNumber);
			canvas.drawText(mText, textX,textY, mPaintText);
		//---------------
			
		/*//设置背景图
		Paint mPaint=new Paint();
		canvas.drawBitmap(mBackGround, 0, 0, mPaint);*/
		
	}
	
	public boolean onTouchEvent(MotionEvent event){
		try{
			if(event.getAction()!=MotionEvent.ACTION_DOWN){
				return super.onTouchEvent(event);
			}
			
			if(event.getAction()== MotionEvent.ACTION_DOWN){
				double x=event.getX();
				double y=event.getY();
				//Adult area
				double adult_x1=w*((68.0)/(720.0));
				double adult_y1=h*(735.0/1280.0);
				double adult_x2=w*(218.0/720.0);
				double adult_y2=h*(805.0/1280.0);
				
				//Child area
				double child_x1=w*(500.0/720.0);
				double child_y1=h*(735.0/1280.0);
				double child_x2=w*(650.0/720.0);
				double child_y2=h*(805.0/1280.0);
				
				//图片展示
				double image_x1=w*(58.0/720.0);
				double image_y1=h*(494.0/1280.0);
				double image_x2=w*(189.0/720.0);
				double image_y2=h*(542.0/1280.0);
				
				//设置
				double setting_x1=w*(58.0/720.0);
				double setting_y1=h*(582.0/1280.0);
				double setting_x2=w*(189.0/720.0);
				double setting_y2=h*(630.0/1280.0);
				
				int area=0;
				if(x>=adult_x1 && x<adult_x2 && y>adult_y1 && y<adult_y2){
					area=1;
				}
				else if(x>=child_x1 && x<child_x2 && y>child_y1 && y<child_y2){
					area=2;
				}				
				else if(x>=image_x1 && x<image_x2 && y>image_y1 && y<image_y2){
					area=3;
				}
				else if(x>=setting_x1 && x<setting_x2 && y>setting_y1 && y<setting_y2){
					area=4;
				}
				
				((testActivity) mcontext).showClickArea(area);
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return super.onTouchEvent(event);
		
	}
	
}
