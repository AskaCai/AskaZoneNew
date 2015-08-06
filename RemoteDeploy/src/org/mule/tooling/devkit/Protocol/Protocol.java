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
	String projectPath; //��Ŀ·��
	String zipParentPath;//zip���ϲ�·��
	String zipName; //zip���ļ���������׺
	LogFile log;
	ZipFile zipFile;
	
	public Protocol(String projectPath, String zipParentPath, String zipName) {
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
	
	public void getProcotolProperty(){
		//����**-protocol.properties�ļ�
		File output = new File(projectPath+File.separator+"protocol.properties");
		log.appendLog("׼������protocol.properties�ļ���" + projectPath+File.separator+"protocol.properties");
	    FileUtil.deleteDir(output);
	    FileUtil.makeDir(output);
	    try {
			output.createNewFile();
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	    log.appendLog("protocol.properties�ļ������ɹ���" + projectPath+File.separator+"protocol.properties");
	    
	    //ͨ��editors.xml��ȡprotocol��Ϣ
	    PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter(output),true);
			String comName = null;
			NodeList list = null;
			Node node =null;
			String nodeVal =null ;
			String command = null;
			String namespace = null;
			log.appendLog("׼����ȡeditor.xml����");
			EditorXml editorXml = new EditorXml(projectPath);
			
			log.appendLog("׼����" + output.getName() + "д��procotol��Ϣ");
			//��ȡ�����
			command = "/namespace";
			list = editorXml.getNodeList(command);
			node = list.item(0);
			nodeVal =node.getAttributes().getNamedItem("prefix").getNodeValue();
			comName =nodeVal;
			pw.println("CompName="+nodeVal);
			log.appendLog("CompName="+nodeVal);
			
			//д��config-ref
			pw.println("config-ref="+comName+"__Configuration_type_strategy");
			log.appendLog("config-ref="+comName+"__Configuration_type_strategy");
			
			//��ȡconfig-type 
			String[] words = comName.split("-");//�������ʲ���-�Ļᱨ����Ҫ�޸�
			String globalCaption = "";
			log.appendLog("words length="+words.length);
			for (int i = 0; i < words.length; i++) {
				log.appendLog("words="+words[i]);
				globalCaption += NodeUtil.firstWordToUpper(words[i]);
			}                                      //��ʱû��
			command = "/namespace/global-cloud-connector[@localId!='parentConfig']";
			log.appendLog("command = " +command);
			list = editorXml.getNodeList(command);
			nodeVal ="";
			if(list!=null&list.getLength()!=0){
				log.appendLog("list.length = "+list.getLength());
				log.appendLog("ֻȡ��һ��node");
				node = list.item(0);
				nodeVal = node.getAttributes().getNamedItem("localId").getNodeValue();
			}
			else{
				log.appendLog("��command�Ҳ����ڵ�list.length = 0");
			}
			pw.println("config-type="+nodeVal);
			log.appendLog("config-type="+nodeVal);
			
			//��ȡnamespace 
			command = "/namespace";
			list = editorXml.getNodeList(command);
			node = list.item(0);
			nodeVal = node.getAttributes().getNamedItem("url").getNodeValue();
			namespace =nodeVal;
			pw.println("namespace=xmlns:"+comName+"='"+nodeVal+"'");
			log.appendLog("namespace=xmlns:"+comName+"=\""+nodeVal+"\"");
			
			//��ȡlocation
			pw.println("location="+namespace+" "+namespace+"/current/mule-"+comName+".xsd");
			log.appendLog("location="+namespace+" "+namespace+"/current/mule-"+comName+".xsd");
			
			//��ȡȫ�ֲ���������ֻ���ǳ�������
			command = "/namespace/global-cloud-connector/attribute-category/group/string";
			list = editorXml.getNodeList(command);
			String globalParameter = NodeUtil.getGlobalParameter(list);
			log.appendLog("command=" +command );
			pw.println("GlobalParameter="+globalParameter);
			log.appendLog("GlobalParameter="+globalParameter);
			
			//��ȡ�����@source����������� ���ڼ䣬����һ�����ֻ��һ��@source����
			Operation[] operations = NodeUtil.getOperationsParameter(editorXml);
			if(operations!=null){
				pw.println("operation="+operations[0].getName());
				pw.println("property="+operations[0].getJson());
				log.appendLog("operation="+operations[0].getName());
				log.appendLog("property="+operations[0].getJson());
			}
			pw.close();
			log.appendLog("��" + output.getName() + "д��procotol��Ϣ���");
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		
		log.appendLog("׼����"+zipName+"-procotol.propertiesѹ����"+zipName+".zip��");
		ZipUtils.addFileToExistZip(zipFile, output, zipName+"-protocol.properties");
		log.appendLog(zipName+"-procotol.propertiesѹ�����");
		
	}
}
