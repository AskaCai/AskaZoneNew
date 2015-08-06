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
	String projectPath; //��Ŀ·��
	String zipParentPath;//zip���ϲ�·��
	String zipName; //zip���ļ���������׺
	LogFile log;
	ZipFile zipFile;
	
	public Transformer(String projectPath, String zipParentPath, String zipName) {
		// TODO �Զ����ɵĹ��캯�����
		this.projectPath = projectPath;
		this.zipParentPath = zipParentPath;
		this.zipName = zipName;
		log = new LogFile();
		try {
			zipFile = new ZipFile(zipParentPath+File.separator+zipName+".zip");
		} catch (ZipException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}
	
	public void getTransformerProperty() {
		//����**-transformre.properties�ļ�
		File output = new File(projectPath+File.separator+"transformer.properties");
		log.appendLog("׼������transformer.properties�ļ���" + projectPath+File.separator+"transformer.properties");
	    FileUtil.deleteDir(output);
	    FileUtil.makeDir(output);
	    try {
			output.createNewFile();
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	    log.appendLog("transformer.properties�ļ������ɹ���" + projectPath+File.separator+"transformer.properties");
	    
	    //ͨ�������ȡTransformer�е���Ϣ
	    log.appendLog("��ʼ���з����ȡtransformer��properties��Ϣ");
		TransformerAnnotation anno = new TransformerAnnotation(projectPath);
		Map<String, Object> types = new HashMap<String, Object>();
		try {
			types = anno.getTypes();
		} catch (SecurityException | IllegalArgumentException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		Class[][] sourceTypes = (Class[][]) types.get("SourceTypes");
		Class[] returnTypes = (Class[]) types.get("ReturnType");
		String[] methodName = (String[])types.get("Methods");
		int methodNum = (int)types.get("MethodNum");//�����ʱ���ã���ΪĬ��һ��connectorֻ��һ��transformer����
		log.appendLog("�ɹ���ȡtransformer��Ϣ");
		
		log.appendLog("��ʼ��transformer.propertiesд������ȡ��Ϣ ");
		//��transformer.properties�ļ���д��ȡ������Ϣ
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
		  				pw.print("&"+sourceTypes[k][i]);//���������&�ָ�
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
			log.appendLog("��transformer.propertiesд�����");
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		};
		
		log.appendLog("׼����"+zipName+"-transformre.propertiesѹ����"+zipName+".zip��");
		ZipUtils.addFileToExistZip(zipFile, output,zipName+"-transformer.properties");
		log.appendLog(zipName+"-transformre.propertiesѹ�����");
	    
	}
	
	//����ƽ̨�ķ�������Ҫ������Ҫת��
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
