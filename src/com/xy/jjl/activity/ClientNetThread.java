package com.xy.jjl.activity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import com.xy.jjl.image_activity.ImageShowActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;


/**
 * 采用socket方式，已不使用。 2016-12-11
 *
 */
public class ClientNetThread extends Thread {
	ImageShowActivity imageShowActivity;
	SettingsActivity settingsActivity;
	
	Socket s=null;
	public DataOutputStream dout;
	public DataInputStream din;
	public boolean flag = true;//是否循环处理标志
	public String ImagePath=null;
	public boolean isExistSDCard=false;
	
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
	
	public ClientNetThread(SettingsActivity settingsActivity){
		this.settingsActivity=settingsActivity;
	}
	
	public ClientNetThread(ImageShowActivity imageShowActivity){
		this.imageShowActivity=imageShowActivity;
	}
	
	public void setPath(String path){
		this.ImagePath=path;
	}
	
	/*public void isExistSDCard(boolean isExist){
		this.isExistSDCard=isExist;
	}*/
	
	public void run(){
		try{//连接网络并打开流
			//s=new Socket("112.74.38.240",8888);
			s=new Socket("192.168.0.112",8888);
			//s.setSoTimeout(3000);//设定超时,防止程序永久挂起
			dout=new DataOutputStream(s.getOutputStream());
			din=new DataInputStream(s.getInputStream());
		}catch(Exception e){
			e.printStackTrace();
		}
		
		while(flag){
			try{
				String msg=din.readUTF();//接收服务器发送来的信息
				System.out.println("server msg = "+msg);
				//下载图片
				if(msg.startsWith("<#IMAGETOCLIENT#>")){
					msg=msg.substring(17);
					String[][]strs=new String[Integer.parseInt(msg)][7]; //7组数据
					ArrayList Infos=new ArrayList();
					for(int i=0;i<strs.length;i++){
						String temp=din.readUTF();
						String[] str=temp.split("\\|");
						Infos.add(str);
					}
					
					ArrayList Images=new ArrayList();
					for(int i=0;i<strs.length;i++){
						int size=din.readInt();
						byte[]bs=new byte[size];
						din.read(bs);
						Images.add(bs);
					}
					
					Message message=new Message();
					Bundle data=new Bundle();
					data.putStringArrayList("Infos", Infos);
					data.putStringArrayList("Images", Images);
					message.what=1;
					message.setData(data);
					//activity.myHandler.sendMessage(message);//发送消息
				}
				else if(msg.startsWith("<#DOWNLOADIMAGE#>")){
 
					String path=ImagePath+File.separator;
					
					//==========
					File file=new File(path);
					try{
						if(file.isDirectory()){
							String name[]=file.list();
							for(int i=0;i<name.length;++i){
								if(name[i].indexOf("jpg")!=-1){
									File f=new File(path,name[i]);
									f.delete();
								}
							}
						}
					}catch(Exception e){
						e.printStackTrace();
					}
					//==========
					
					//==========
					try{
						
						FileOutputStream fos=null;
						boolean mflag=true;
						while(mflag){
							int picNumber=0;
							int len=0;	
							int beginPos=0;
							int endPos=0;
							int tLen=0;
							boolean isHead=false;
							boolean isEnd=false;
							
							byte[]tdata=null;
							byte[]data=new byte[1024];
							while((len=din.read(data,0,data.length))!=-1){
								
								//=============================== 
								
								for(int i=0;i<data.length;++i){
									if(i<data.length-8){
										if(data[i+0]==PICTURE_PACKAGE_HEAD[0] && 
										   data[i+1]==PICTURE_PACKAGE_HEAD[1] &&
										   data[i+2]==PICTURE_PACKAGE_HEAD[2] &&
										   data[i+3]==PICTURE_PACKAGE_HEAD[3] &&
										   data[i+4]==PICTURE_PACKAGE_HEAD[4] &&
										   data[i+5]==PICTURE_PACKAGE_HEAD[5] &&
										   data[i+6]==PICTURE_PACKAGE_HEAD[6] &&
										   data[i+7]==PICTURE_PACKAGE_HEAD[7] ){
											beginPos=i+8;
											isHead=true;										
											picNumber++;
											break;											
										}else{
											isHead=false;
										}
											
									}else{
										break;
									}										
								}
								
								if(len<1024){
									for(int i=0;i<data.length;++i){
										
										if(i<data.length-4){
											if(data[i+0]==PICTURE_PACKAGE_END[0] && 
											   data[i+1]==PICTURE_PACKAGE_END[1] &&
											   data[i+2]==PICTURE_PACKAGE_END[2] &&
											   data[i+3]==PICTURE_PACKAGE_END[3] ){												
												endPos=i;
												isEnd=true;
												break;
											}
										}else{
											break;
										}
									}
								}
								//===============================
								if(isHead){								
									if(fos!=null){
										//----------------
										tLen=beginPos;
										tdata=null;
										tdata=new byte[tLen];
										System.arraycopy(data, 0, tdata, 0, tLen);
										fos.write(tdata,0,tdata.length);
										//----------------
										fos.flush();
										fos.close();
										fos=null;
									} 
									
									tLen=len-beginPos;
									tdata=null;
									tdata=new byte[tLen];
									System.arraycopy(data, beginPos, tdata, 0, tdata.length);
									
									String fullPath=path +"tmsa"+ picNumber + ".jpg";
									fos=new FileOutputStream(new File(fullPath));	
									fos.write(tdata,0,tdata.length);
									fos.flush();
								}
								
								if(isHead==false && isEnd==false && fos!=null){
									fos.write(data,0,len);
									fos.flush();
								}								
								
								if(isEnd){
									
									tLen=endPos;
									tdata=null;
									tdata=new byte[tLen];
									System.arraycopy(data, 0, tdata, 0, tLen);
									
									fos.write(tdata,0,tdata.length);
									fos.flush();
									//-----
									fos.close();
									fos=null;
									mflag=false;
									break;									
								}
								
								data=null;
								data=new byte[1024];								 
							}
							
							mflag=false;
							
							settingsActivity.myDialog.dismiss();
							Message message=new Message();
							message.what=0;
							settingsActivity.myHandler.sendMessage(message);//发送消息
							
							//----------------
						}
					}catch(Exception e){
						e.printStackTrace();
					}
 
					//==========
				}
				else if(msg.startsWith("<#DOWNLOAD IMAGE NULL#>")){
					settingsActivity.myDialog.dismiss();
					Message message=new Message();
					message.what=1;
					settingsActivity.myHandler.sendMessage(message);//发送消息
					
				}
				else if(msg.startsWith("<#DOWNLOADOK#>")){//下载完成标志字符
					
				}
				else if(msg.startsWith("<#UPLOADOK#>")){//上传完成标志字符
					
				}
				
			}catch(IOException e){
				e.printStackTrace();
			}finally{
				try{
					if(din!=null){
						din.close();
						din=null;
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				
				try{
					if(dout!=null){
						dout.close();
						dout=null;
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				
				try{
					if(s!=null){
						s.close();
						s=null;
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				
				flag=false;//关闭Thread
			}
		}
		
	}
	
}
