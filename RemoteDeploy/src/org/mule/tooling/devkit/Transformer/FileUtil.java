package org.mule.tooling.devkit.Transformer;


import java.io.File;

public class FileUtil {
	public static boolean deleteDir(File destFile){
		boolean flag = false;  
	    // ·��Ϊ�ļ��Ҳ�Ϊ�������ɾ��  
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
