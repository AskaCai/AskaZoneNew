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
		// TODO �Զ����ɵĹ��캯�����
		log = new LogFile();
		this.projectPath = projectPath;
		String dir = projectPath;
		dir = dir + File.separator + "target"+File.separator+"update-site"+File.separator+"plugins";
		log.appendLog("Jar������·����" + dir);
		if(unzipJar(dir)){
			editorXmlPath = dir + File.separator + "editors.xml";
			log.appendLog("editors.xml·��Ϊ��" + editorXmlPath);
			try {
				this.doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(editorXmlPath);
				log.appendLog("��ȡeditor.xml�ɹ�");
			} catch (SAXException e) {
				// TODO �Զ����ɵ� catch ��
				log.appendLog("��ȡeditor.xmlʧ��");
				e.printStackTrace();
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				log.appendLog("��ȡeditor.xmlʧ��");
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				log.appendLog("��ȡeditor.xmlʧ��");
				// TODO �Զ����ɵ� catch ��
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
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		return list;
	}
	public boolean unzipJar(String path){
		File[] jarFiles = getJars(path);
		File protocolJar = jarFiles[0];
		log.appendLog("׼����ѹ��jar��Ϊ��" + protocolJar.getAbsolutePath());
		boolean tag =false;
		
		tag = ZipUtils.extractAllFiles(protocolJar.getAbsolutePath(), protocolJar.getParent());
		if(tag){
			log.appendLog("��ѹ�ɹ�");
		}
		else{
			log.appendLog("��ѹʧ��");
		}
		return tag;
	}
	public File[] getJars(String path){
		// ϵͳ���·��
		File libPath = new File(path);

		// ��ȡ���е�.jar��.zip�ļ�
		File[] jarFiles = libPath.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".jar");
			}
		});
		return jarFiles;
	}
}

