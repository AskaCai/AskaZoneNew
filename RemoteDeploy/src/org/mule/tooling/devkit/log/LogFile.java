package org.mule.tooling.devkit.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogFile {
	File logFile;
	public LogFile() {
		// TODO 自动生成的构造函数存根
		String eclipsePath = System.getProperty("user.dir");
//		eclipsePath = "C:\\ZipTest";
		logFile = new File(eclipsePath + File.separator + "log.txt");
		createNewFile(logFile);
		
	}
	
	public void appendLog(String context){
		try {
			FileWriter fw = new FileWriter(logFile,true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.append(getFormatTime() + " " + context + "\r\n");
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	public static String getFormatTime(){
		Date nowTime=new Date(); 
		SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss +SSS"); 
		return "["+time.format(nowTime)+"]"; 
	}
	public static boolean createNewFile(File destFile){
		boolean flag = true;
		if(!destFile.getParentFile().exists()){
			if(!destFile.getParentFile().mkdirs()){ 
				return false; 
			} 
		}
		if(!destFile.exists()){
			try {
				destFile.createNewFile();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				return false;
			}
		}
		return flag;
	}
	
	public static void main(String[] args) {
		LogFile logA = new LogFile();
		LogFile logB = new LogFile();
		int i = 1;
		for(; i<11 ; i++){
			logA.appendLog("LogA  第"+i+"行，hello world!");
			logB.appendLog("LogB");
		}
	}
}
