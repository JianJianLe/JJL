package com.xy.jjl.utils;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

import com.google.gson.internal.$Gson$Types;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.xy.jjl.utils.ProgressUploadFile.ProgressListener;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.TimeUnit;
/**
 * Description : OkHttp网络连接封装工具类 
 * Author : ldong 
 * Date   : 16/1/31 
 */  
 
public class OkHttpUtils {  
  
    private static final String TAG = "OkHttpUtils";  
    
    private static final MediaType MEDIA_TYPE_JPG=MediaType.parse("image/jpg");
    private static OkHttpUtils mInstance;  
    private OkHttpClient mOkHttpClient;
    private Handler mHandler;
    
    //long totalBytes, long remainingBytes, boolean done
    private long totalBytes=0;
    private long remainingBytes=0;
    
  
    private OkHttpUtils() {  
        /** 
         * 构建OkHttpClient 
         */  
        mOkHttpClient = new OkHttpClient();  
        /** 
         * 设置连接的超时时间 
         */  
        mOkHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        /** 
         * 设置响应的超时时间 
         */  
        mOkHttpClient.setWriteTimeout(10, TimeUnit.SECONDS);
        /** 
         * 请求的超时时间 
         */  
        mOkHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
        /** 
         * 允许使用Cookie 
         */  
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        /** 
         * 获取主线程的handler 
         */  
        mHandler = new Handler(Looper.getMainLooper());
    }  
  
    /** 
     * 通过单例模式构造对象 
     * @return OkHttpUtils 
     */  
    private synchronized static OkHttpUtils getmInstance() {  
        if (mInstance == null) {  
            mInstance = new OkHttpUtils();  
        }  
        return mInstance;  
    }  
    
    /** 
     * 构造Get请求 
     * @param url  请求的url 
     * @param callback  结果回调的方法 
     */  
    private void getRequest(String url, final ResultCallback callback) {  
        final Request request = new Request.Builder()
        							.url(url)
        							.build();
        deliveryResult(callback, request);  
    }  
    
    /** 
     * 构造post 请求 
     * @param url 请求的url 
     * @param callback 结果回调的方法 
     * @param params 请求参数 
     */  
    private void postRequest(String url, final ResultCallback callback, List<Param> params) {
        Request request = buildPostRequest(url, params);  
        deliveryResult(callback, request);  
    }  
    
    /**
     * 构造post 请求,文件上传
     * @param url
     * @param callback
     * @param files
     * @param fileKeys
     * @throws IOException
     *
     */

	private void postRequest(String url,final ResultCallback callback,
    						 File[] files,String[] fileKeys,
    						 List<Param> params){
    	
    	Request request=buildMultipartFormRequest(url,files,fileKeys,params);
    	deliveryResult(callback,request);
    }

	
    
    /**
     * 下载文件
     * @param url
     * @param destFileDir 本地文件存储的文件夹
     * @param callback
     * @throws IOException
     *
     */

	private void download(final String url,
						  final String destFileDir,
						  final ResultCallback callback,
						  List<Param> params){

        FormEncodingBuilder builder = new FormEncodingBuilder();  
        
        for (Param param : params) {  
            builder.add(param.key, param.value);  
        }  
        RequestBody requestBody = builder.build();  
        
    	final Request request=new Request.Builder()
    			.url(url)
    			.post(requestBody)
    			.build();
    	final Call call=mOkHttpClient.newCall(request);
    	call.enqueue(new Callback(){

			@Override
			public void onFailure(Request arg0, IOException e) {
				// TODO Auto-generated method stub
				sendFailCallback(callback, e);
			}

			@Override
			public void onResponse(Response response)  {
				// TODO Auto-generated method stub
				InputStream is=null;
				byte[] buf=new byte[2048];
				int len=0;
				FileOutputStream fos=null;
				try{
					String temp=null;
					temp=response.header("Content-Disposition");
					String[] str=temp.split(";");
					String[] strtemp=str[1].split("=");
					String filename=strtemp[1].replace("\"", "");
					String fullname=destFileDir+filename;
					is=response.body().byteStream();
					int mlen=is.available();
					File file=new File(destFileDir,filename);
					fos=new FileOutputStream(file);
					while((len=is.read(buf))!=-1){
						fos.write(buf,0,len);
					}
					fos.flush();
					//如果下载成功，返回绝对路径
					sendSuccessCallBack(callback, file.getAbsolutePath());
					
				}catch(IOException e){
					sendFailCallback(callback, e);
				}finally{
					try{
						if(is!=null) is.close();
					}catch(IOException e){
						
					}
					try{
						if(fos!=null) fos.close();
					}catch(IOException e){
						
					}
					
				}
			}
    		
    		
    	});
		
    }
    
	private String getFileName(String path){
		
		int separatorIndex = path.lastIndexOf("/");
  		return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());

	}
 
    /** 
     * 处理请求结果的回调 
     * @param callback 
     * @param request 
     */  
    private void deliveryResult(final ResultCallback callback, Request request) {  
  
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override  
            public void onFailure(Request request, final IOException e) {
                sendFailCallback(callback, e);  
            }  
  
            @Override  
            public void onResponse(Response response) throws IOException {  
                try {  
                    String str = response.body().string();  
//                    if (callback.mType == String.class) {
                        sendSuccessCallBack(callback, str);  
//                    } else {
//                        Object object = new JSONObject(str);
//                        sendSuccessCallBack(callback, object);
//                    }
                } catch (final Exception e) {  
                    sendFailCallback(callback, e);
                }  
  
            }  
        });  
    }  
  
    /** 
     * 发送失败的回调 
     * @param callback 
     * @param e 
     */  
    private void sendFailCallback(final ResultCallback callback, final Exception e) {  
        mHandler.post(new Runnable() {  
            @Override  
            public void run() {  
                if (callback != null) {  
                    callback.onFailure(e);  
                }  
            }  
        });  
    }  
  
    /** 
     * 发送成功的调 
     * @param callback 
     * @param obj 
     */  
    private void sendSuccessCallBack(final ResultCallback callback, final Object obj) {  
        mHandler.post(new Runnable() {  
            @Override  
            public void run() {  
                if (callback != null) {  
                    callback.onSuccess(obj);  
                }  
            }  
        });  
    }  
  
    /** 
     * 构造post请求 
     * @param url  请求url 
     * @param params 请求的参数 
     * @return 返回 Request 
     */  
    private Request buildPostRequest(String url, List<Param> params) {  
    	
        FormEncodingBuilder builder = new FormEncodingBuilder();  
        
        for (Param param : params) {  
            builder.add(param.key, param.value);  
        }  
        RequestBody requestBody = builder.build();  
        return new Request.Builder()
        				.url(url)
        				.post(requestBody)
        				.build();  
    }  
    
    
    //=============================
    /** 
     * uploadFile: 上传一个文件，带进度
     * @param url  请求url 
     * @param params 请求的参数 
     * @return 返回 Request 
     */  
    private Request uploadFile(String url,String filePath,List<Param> params){
    	
    	MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);

        File file = new File(filePath);
        builder.addFormDataPart("file", file.getName(), createCustomRequestBody(MultipartBuilder.FORM, file, new ProgressListener() {
            @Override 
            public void onProgress(long totalBytes, long remainingBytes, boolean done) {
               //System.out.print((totalBytes - remainingBytes) * 100 / totalBytes + "%");
            	OkHttpUtils.this.totalBytes=totalBytes;
            	OkHttpUtils.this.remainingBytes=remainingBytes;
            }
        }));
    	RequestBody requestBody = builder.build(); 
    	return new Request.Builder()
    					.url(url)
    					.post(requestBody)
    					.build();
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
  //=============================
    /**********************对外接口************************/  
  
    /** 
     * get请求 
     * @param url  请求url 
     * @param callback  请求回调 
     */  
    public static void get(String url, ResultCallback callback) {  
        getmInstance().getRequest(url, callback);  
    }  
  
    /** 
     * post请求 
     * @param url       请求url 
     * @param callback  请求回调 
     * @param params    请求参数 
     */  
    public static void post(String url, final ResultCallback callback, List<Param> params) {  
        getmInstance().postRequest(url, callback, params);  
    }  

    /** 
     * 
     * @param url     
     * @param files   
     * @param fileKeys    
     * @param params
     */ 
    public static void uploadImage(String url,final ResultCallback callback,
			 File[] files,String[] fileKeys,
			 List<Param> params){
    	
    	getmInstance().postRequest(url, callback, files, fileKeys, params);
    }
    
    
    public static void downloadImge(final String url,
								    final String destFileDir,
								    final ResultCallback callback,
								    List<Param> params){
    	getmInstance().download(url, destFileDir, callback,params);
    }
    /** 
     * 
     * @param url     
     * @param files   
     * @param fileKeys    
     * @param params
     */ 

    //*********************************************
    
    private Request buildMultipartFormRequest(String url,File[] files,
    										  String[] fileKeys,List<Param> params){
    	MultipartBuilder builder=new MultipartBuilder()
    			.type(MultipartBuilder.FORM);
    			
    	
    	//传参数
    	for(Param param : params){
    		builder.addPart(Headers.of("Content-Disposition","form-data; name=\"" + param.key + "\""),
    				RequestBody.create(null, param.value));
    	}
    	
    	//传文件
    	if(files!=null){
    		RequestBody fileBody=null;
    		for(int i=0;i<files.length;i++){
    			File file=files[i];
    			String fileName=file.getName();
    			fileBody=RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
    			//TODO 根据文件名设置contentType
    			builder.addPart(Headers.of("Content-Disposition",
    					"form-data; name=\"" + fileKeys[i] + "\"; filename=\"" + fileName + "\""),
    					fileBody);
    					
    		}
    	}
    	
    	RequestBody requestBody=builder.build();
    	return new Request.Builder()
    			.url(url)
    			.post(requestBody)
    			.build();
    }
    
    private String guessMimeType(String path){
    	FileNameMap fileNameMap=URLConnection.getFileNameMap();
    	String contentTypeFor=fileNameMap.getContentTypeFor(path);
    	if(contentTypeFor==null){
    		contentTypeFor="application/octet-stream";
    	}
    	return contentTypeFor;
    }
    
    /** 
     * http请求回调类,回调方法在UI线程中执行 
     * @param <T> 
     */  
    public static abstract class ResultCallback<T> {  
  
        Type mType;  
  
        public ResultCallback(){  
            mType = getSuperclassTypeParameter(getClass());
        }  
  
        static Type getSuperclassTypeParameter(Class<?> subclass) {  
            Type superclass = subclass.getGenericSuperclass();//返回父类的类型  
            if (superclass instanceof Class) {  
                throw new RuntimeException("Missing type parameter.");  
            }  
            ParameterizedType parameterized = (ParameterizedType) superclass;  
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);  
        }  
  
        /** 
         * 请求成功回调 
         * @param response 
         */  
        public abstract void onSuccess(T response);  
  
        /** 
         * 请求失败回调 
         * @param e 
         */  
        public abstract void onFailure(Exception e);  
    }  
  
    /** 
     * post请求参数类 
     */  
    public static class Param {  
  
    	public String key;//请求的参数  
        public String value;//参数的值  
  
        public Param() {  
        }  
  
        public Param(String key, String value) {  
            this.key = key;  
            this.value = value;  
        }  
  
    }  
 
}  