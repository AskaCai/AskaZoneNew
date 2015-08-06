package org.mule.tooling.devkit.xml;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import net.lingala.zip4j.core.ZipFile;

import org.mule.tooling.devkit.log.LogFile;
import org.mule.tooling.devkit.zip.ZipUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class EditorXml {
	private String projectPath;
	Document doc;
	private String editorXmlPath;
	private LogFile log;
	public EditorXml(String projectPath) {
		// TODO 自动生成的构造函数存根
		log = new LogFile();
		this.projectPath = projectPath;
		String dir = projectPath;
		dir = dir + File.separator + "target"+File.separator+"update-site"+File.separator+"plugins";
		log.appendLog("Jar包所在路径：" + dir);
		if(unzipJar(dir)){
			editorXmlPath = dir + File.separator + "editors.xml";
			log.appendLog("editors.xml路径为：" + editorXmlPath);
			try {
				this.doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(editorXmlPath);
				log.appendLog("获取editor.xml成功");
			} catch (SAXException e) {
				// TODO 自动生成的 catch 块
				log.appendLog("获取editor.xml失败");
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				log.appendLog("获取editor.xml失败");
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				log.appendLog("获取editor.xml失败");
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}
	public NodeList getNodeList(String command){
		XPathFactory factory = XPathFactory.newInstance();
		XPath xPath = factory.newXPath();
		NodeList list = null;
		try {
			list = (NodeList)xPath.evaluate(command, this.doc, XPathConstants.NODESET);	
		} catch (XPathExpressionException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return list;
	}
	public boolean unzipJar(String path){
		File[] jarFiles = getJars(path);
		File protocolJar = jarFiles[0];
		log.appendLog("准备解压的jar包为：" + protocolJar.getAbsolutePath());
		boolean tag =false;
		
		tag = ZipUtils.extractAllFiles(protocolJar.getAbsolutePath(), protocolJar.getParent());
		if(tag){
			log.appendLog("解压成功");
		}
		else{
			log.appendLog("解压失败");
		}
		return tag;
	}
	public File[] getJars(String path){
		// 系统类库路径
		File libPath = new File(path);

		// 获取所有的.jar和.zip文件
		File[] jarFiles = libPath.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".jar");
			}
		});
		return jarFiles;
	}
}

