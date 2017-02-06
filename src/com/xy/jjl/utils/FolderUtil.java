package com.xy.jjl.utils;

import java.io.File;

public class FolderUtil {
	//private String path=null;
	
	public static void DeleteJPGfile(String path){
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
	}

}
