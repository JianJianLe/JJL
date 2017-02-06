package com.xy.jjl.common;

public class APPConfig {
	
	//public final static String BaseLink="http://192.168.0.112:8080/";
	//public final static String BaseUrl="http://192.168.0.112:8080/JJLserver";
	
	public final static String BaseLink="http://112.74.38.240:8080/";	
	public final static String BaseUrl="http://112.74.38.240:8080/JJLserver";
	
	public final static String Login=BaseUrl + "/loginServlet";//登陆
	
	public final static String Register=BaseUrl + "/registerServlet";//注册
	
	public final static String uploadImage=BaseUrl + "/imageUploadServlet";//上传图片
	
	public final static String Media=BaseUrl + "/mediaServlet";
	
	public final static String uploadFile=BaseUrl + "/uploadFileServlet";//上传文件 or 视频
	
	public final static String nameListUrl=BaseUrl+"/nameListServlet";//获取所有用户名
	
	public final static String personalDetail=BaseUrl+"/personalDetailServlet";//获取用户信息
	
	public final static String changePassword=BaseUrl+"/changePasswordServlet";//修改用户密码
	
	public final static String videoDetail=BaseUrl+"/videoDetailServlet";//获取视频信息，用于下载视频
	
	public final static String imageDetail=BaseUrl+"/imageDetailServlet";//获取图片信息，用于下载图片
	
	public final static String updateAPK=BaseUrl+"/updateAPKServlet";
	public final static String APKlink=BaseLink+"/APK/JJL.apk";
	
	//key
	public final static String SHARE_PATH = "share_path";
	
	public final static String SETTING = "setting";

}
