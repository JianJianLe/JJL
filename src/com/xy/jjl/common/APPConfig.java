package com.xy.jjl.common;

public class APPConfig {
	
	//public final static String BaseLink="http://192.168.0.112:8080/";
	//public final static String BaseUrl="http://192.168.0.112:8080/JJLserver";
	
	public final static String BaseLink="http://112.74.38.240:8080/";	
	public final static String BaseUrl="http://112.74.38.240:8080/JJLserver";
	
	public final static String Login=BaseUrl + "/loginServlet";//��½
	
	public final static String Register=BaseUrl + "/registerServlet";//ע��
	
	public final static String uploadImage=BaseUrl + "/imageUploadServlet";//�ϴ�ͼƬ
	
	public final static String Media=BaseUrl + "/mediaServlet";
	
	public final static String uploadFile=BaseUrl + "/uploadFileServlet";//�ϴ��ļ� or ��Ƶ
	
	public final static String nameListUrl=BaseUrl+"/nameListServlet";//��ȡ�����û���
	
	public final static String personalDetail=BaseUrl+"/personalDetailServlet";//��ȡ�û���Ϣ
	
	public final static String changePassword=BaseUrl+"/changePasswordServlet";//�޸��û�����
	
	public final static String videoDetail=BaseUrl+"/videoDetailServlet";//��ȡ��Ƶ��Ϣ������������Ƶ
	
	public final static String imageDetail=BaseUrl+"/imageDetailServlet";//��ȡͼƬ��Ϣ����������ͼƬ
	
	public final static String updateAPK=BaseUrl+"/updateAPKServlet";
	public final static String APKlink=BaseLink+"/APK/JJL.apk";
	
	//key
	public final static String SHARE_PATH = "share_path";
	
	public final static String SETTING = "setting";

}
