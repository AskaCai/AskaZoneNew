package org.mule.tooling.devkit.Transformer;


import java.io.File;

public class FileUtil {
	public static boolean deleteDir(File destFile){
		boolean flag = false;  
	    // 路径为文件且不为空则进行删除  
	    if (destFile.isFile() && destFile.exists()) {  
	    	destFile.delete();  
		        flag = true;  
	    }  
	    return flag; 

	}
	public static boolean makeDir(File destFile){
		boolean flag = true;
		if(!destFile.getParentFile().exists()){
			if(!destFile.getParentFile().mkdirs()){ 
				return false; 
			} 
		}
		return flag;
	}
}
