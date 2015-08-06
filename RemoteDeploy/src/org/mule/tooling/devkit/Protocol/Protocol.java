package org.mule.tooling.devkit.Protocol;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.mule.tooling.devkit.Transformer.FileUtil;
import org.mule.tooling.devkit.log.LogFile;
import org.mule.tooling.devkit.xml.EditorXml;
import org.mule.tooling.devkit.xml.NodeUtil;
import org.mule.tooling.devkit.xml.Operation;
import org.mule.tooling.devkit.zip.ZipUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Protocol {
	String projectPath; //项目路径
	String zipParentPath;//zip包上层路径
	String zipName; //zip包文件名不带后缀
	LogFile log;
	ZipFile zipFile;
	
	public Protocol(String projectPath, String zipParentPath, String zipName) {
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
	
	public void getProcotolProperty(){
		//创建**-protocol.properties文件
		File output = new File(projectPath+File.separator+"protocol.properties");
		log.appendLog("准备创建protocol.properties文件：" + projectPath+File.separator+"protocol.properties");
	    FileUtil.deleteDir(output);
	    FileUtil.makeDir(output);
	    try {
			output.createNewFile();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	    log.appendLog("protocol.properties文件创建成功：" + projectPath+File.separator+"protocol.properties");
	    
	    //通过editors.xml获取protocol信息
	    PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter(output),true);
			String comName = null;
			NodeList list = null;
			Node node =null;
			String nodeVal =null ;
			String command = null;
			String namespace = null;
			log.appendLog("准备获取editor.xml对象");
			EditorXml editorXml = new EditorXml(projectPath);
			
			log.appendLog("准备往" + output.getName() + "写入procotol信息");
			//获取组件名
			command = "/namespace";
			list = editorXml.getNodeList(command);
			node = list.item(0);
			nodeVal =node.getAttributes().getNamedItem("prefix").getNodeValue();
			comName =nodeVal;
			pw.println("CompName="+nodeVal);
			log.appendLog("CompName="+nodeVal);
			
			//写入config-ref
			pw.println("config-ref="+comName+"__Configuration_type_strategy");
			log.appendLog("config-ref="+comName+"__Configuration_type_strategy");
			
			//获取config-type 
			String[] words = comName.split("-");//单个单词不带-的会报错，需要修改
			String globalCaption = "";
			log.appendLog("words length="+words.length);
			for (int i = 0; i < words.length; i++) {
				log.appendLog("words="+words[i]);
				globalCaption += NodeUtil.firstWordToUpper(words[i]);
			}                                      //暂时没用
			command = "/namespace/global-cloud-connector[@localId!='parentConfig']";
			log.appendLog("command = " +command);
			list = editorXml.getNodeList(command);
			nodeVal ="";
			if(list!=null&list.getLength()!=0){
				log.appendLog("list.length = "+list.getLength());
				log.appendLog("只取第一个node");
				node = list.item(0);
				nodeVal = node.getAttributes().getNamedItem("localId").getNodeValue();
			}
			else{
				log.appendLog("此command找不到节点list.length = 0");
			}
			pw.println("config-type="+nodeVal);
			log.appendLog("config-type="+nodeVal);
			
			//获取namespace 
			command = "/namespace";
			list = editorXml.getNodeList(command);
			node = list.item(0);
			nodeVal = node.getAttributes().getNamedItem("url").getNodeValue();
			namespace =nodeVal;
			pw.println("namespace=xmlns:"+comName+"='"+nodeVal+"'");
			log.appendLog("namespace=xmlns:"+comName+"=\""+nodeVal+"\"");
			
			//获取location
			pw.println("location="+namespace+" "+namespace+"/current/mule-"+comName+".xsd");
			log.appendLog("location="+namespace+" "+namespace+"/current/mule-"+comName+".xsd");
			
			//获取全局参数，这里只考虑常见类型
			command = "/namespace/global-cloud-connector/attribute-category/group/string";
			list = editorXml.getNodeList(command);
			String globalParameter = NodeUtil.getGlobalParameter(list);
			log.appendLog("command=" +command );
			pw.println("GlobalParameter="+globalParameter);
			log.appendLog("GlobalParameter="+globalParameter);
			
			//获取插件内@source方法及其参数 简单期间，假设一个插件只有一个@source方法
			Operation[] operations = NodeUtil.getOperationsParameter(editorXml);
			if(operations!=null){
				pw.println("operation="+operations[0].getName());
				pw.println("property="+operations[0].getJson());
				log.appendLog("operation="+operations[0].getName());
				log.appendLog("property="+operations[0].getJson());
			}
			pw.close();
			log.appendLog("往" + output.getName() + "写入procotol信息完成");
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		log.appendLog("准备将"+zipName+"-procotol.properties压缩到"+zipName+".zip中");
		ZipUtils.addFileToExistZip(zipFile, output, zipName+"-protocol.properties");
		log.appendLog(zipName+"-procotol.properties压缩完成");
		
	}
}
