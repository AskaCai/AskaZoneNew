package org.mule.tooling.devkit.Transformer;


import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.mule.tooling.devkit.log.LogFile;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ConnectorInfo{
	LogFile log;
	String projectPath = null;
	public static StringBuilder className = new StringBuilder();
	StringBuilder packageName = new StringBuilder("org.mule.modules.");
	public static StringBuilder endPackageName = new StringBuilder();
	
	public ConnectorInfo(String projectPath) {
		// TODO 自动生成的构造函数存根
		this.projectPath = projectPath;
		log = new LogFile();
	}
	public String getConnectorPath(){
		//获取xml文件
		String dir = projectPath;
		dir = dir + File.separator + "pom.xml";
		Document doc = null;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(dir);
		} catch (SAXException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

		//获取pom.xml的artifactId节点的内容
		XPathFactory factory = XPathFactory.newInstance();
		XPath xPath = factory.newXPath();
		NodeList list = null;
		try {
			list = (NodeList)xPath.evaluate("/project/artifactId[1]", doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		Node node = list.item(0);
		
		//节点内容进行转换，转换成Connector类名
		className.setLength(0);
		String[] names = node.getTextContent().split("-");
		for(String name : names){
			if(!name.equalsIgnoreCase("connector")){
				packageName.append(name);
				endPackageName.append(name);
			}
			name = (new StringBuilder()).append(Character.toUpperCase(name.charAt(0))).append(name.substring(1)).toString();
			className.append(name);
		}
		log.appendLog("Connector包路径："+packageName);
		log.appendLog("Connector可能的类名为："+ className.toString());
		return packageName.toString()+ "." + getRealClassName(className.toString());
	}
	
	private String getRealClassName(String className) {
		log.appendLog("准备开始获取真实的Connector类名");
		log.appendLog("项目所在路径：" + projectPath);
		String classPath = projectPath + File.separator +"target" + File.separator + "classes" +
							getPackageName2Path();
		log.appendLog("rootPath:" + classPath);
		File rootFile = new File(classPath);
		log.appendLog("Connector类存在的根路径："+rootFile.getAbsolutePath());
		if(rootFile.exists()) {
			if(rootFile.isDirectory()){
				Stack<File> stack = new Stack<>();
				stack.push(rootFile);
				
				while (stack.isEmpty() == false) { //遍历所有类
					File path = stack.pop();
					File[] classFiles = path.listFiles(new FileFilter() {
						public boolean accept(File pathname) {
							return pathname.isDirectory() || pathname.getName().endsWith(".class");
						}
					});
					if(classFiles.length > 0) {
						for (File subFile : classFiles) {
							if (subFile.isDirectory()) {
								stack.push(subFile);
							} 
							else {
								String subFileName = subFile.getName();
								subFileName = subFileName.substring(0, subFileName.lastIndexOf("."));
								if(subFileName.equalsIgnoreCase(className)){
									className = subFileName;
									log.appendLog("找到Connector类名" + subFileName);
								}
								log.appendLog("类名：" + subFileName+"不是Connector类名");
							}
						}
					}
				}
			}	
		}
		return className;
	}
	public String getPackageName2Path(){
		String[] names = packageName.toString().split("\\.");
		String packagePath = "";
		for(String name : names){
			System.out.println(name);
			packagePath += File.separator+name;
		}
		return packagePath;
	}
	public String getPackagePath(){
		StringBuilder packagePath = new StringBuilder(projectPath+File.separator+"src"+File.separator+"main"+File.separator+
				"java"+File.separator+"org"+File.separator+"mule"+File.separator+"modules"+File.separator);
		endPackageName.setLength(0);
		getConnectorPath();
		packagePath.append(endPackageName.toString());
		return packagePath.toString();
	}
}
