package org.mule.tooling.devkit.Transformer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.mule.tooling.devkit.Transformer.FileUtil;
import org.mule.tooling.devkit.Transformer.TransformerAnnotation;
import org.mule.tooling.devkit.log.LogFile;
import org.mule.tooling.devkit.zip.ZipUtils;

public class Transformer {
	String projectPath; //项目路径
	String zipParentPath;//zip包上层路径
	String zipName; //zip包文件名不带后缀
	LogFile log;
	ZipFile zipFile;
	
	public Transformer(String projectPath, String zipParentPath, String zipName) {
		// TODO 自动生成的构造函数存根
		this.projectPath = projectPath;
		this.zipParentPath = zipParentPath;
		this.zipName = zipName;
		log = new LogFile();
		try {
			zipFile = new ZipFile(zipParentPath+File.separator+zipName+".zip");
		} catch (ZipException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	public void getTransformerProperty() {
		//创建**-transformre.properties文件
		File output = new File(projectPath+File.separator+"transformer.properties");
		log.appendLog("准备创建transformer.properties文件：" + projectPath+File.separator+"transformer.properties");
	    FileUtil.deleteDir(output);
	    FileUtil.makeDir(output);
	    try {
			output.createNewFile();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	    log.appendLog("transformer.properties文件创建成功：" + projectPath+File.separator+"transformer.properties");
	    
	    //通过反射获取Transformer中的信息
	    log.appendLog("开始进行反射获取transformer的properties信息");
		TransformerAnnotation anno = new TransformerAnnotation(projectPath);
		Map<String, Object> types = new HashMap<String, Object>();
		try {
			types = anno.getTypes();
		} catch (SecurityException | IllegalArgumentException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		Class[][] sourceTypes = (Class[][]) types.get("SourceTypes");
		Class[] returnTypes = (Class[]) types.get("ReturnType");
		String[] methodName = (String[])types.get("Methods");
		int methodNum = (int)types.get("MethodNum");//这个暂时不用，因为默认一个connector只有一个transformer方法
		log.appendLog("成功获取transformer信息");
		
		log.appendLog("开始往transformer.properties写入所获取信息 ");
		//往transformer.properties文件中写获取到的信息
		PrintWriter pw;
		try {
			pw = new PrintWriter(new FileWriter(output),true);
			int k = 0;
			pw.print("SourceTypes=");
			if(sourceTypes[k]!=null){
		  		for(int i = 0; i<sourceTypes[k].length; i++){
		  			if(i == 0)
		  				pw.print(sourceTypes[k][i]);
		  			else
		  				pw.print("&"+sourceTypes[k][i]);//多个输入用&分隔
		  		}
			}
			pw.println();
			pw.print("ReturnType=");
			if(returnTypes[k]!=null){
				pw.print(returnTypes[k]);
			}
			pw.println();
			pw.print("Methods=");
			if(methodName[k]!=null){
				pw.println(convertName(methodName[k]));
			}
			pw.println();
			pw.close();	
			log.appendLog("往transformer.properties写入完成");
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		};
		
		log.appendLog("准备将"+zipName+"-transformre.properties压缩到"+zipName+".zip中");
		ZipUtils.addFileToExistZip(zipFile, output,zipName+"-transformer.properties");
		log.appendLog(zipName+"-transformre.properties压缩完成");
	    
	}
	
	//集成平台的方法名有要求，所有要转换
	public String convertName(String methodName){
		StringBuilder convertName = new StringBuilder();
		char c;
		int index =0;
		for(int i = 1;i<methodName.length();i++){
			c =methodName.charAt(i);
			if(Character.isUpperCase(c)){
				convertName.append(methodName.substring(index, i)+"-");
				index = i;
			}
		}
		convertName.append(methodName.substring(index, methodName.length()));
		return convertName.toString().toLowerCase();
	} 
}
