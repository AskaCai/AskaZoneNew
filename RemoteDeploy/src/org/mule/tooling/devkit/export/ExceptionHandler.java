package org.mule.tooling.devkit.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import net.lingala.zip4j.core.ZipFile;

import org.mule.tooling.devkit.Processor.Processor;
import org.mule.tooling.devkit.Protocol.Protocol;
import org.mule.tooling.devkit.log.LogFile;

public class ExceptionHandler {
	static String projectPath ;
	static String zipPath;
	static String isRemoteDeploy;
	static String remoteUrl;
	static int port;
	static String connectorType; //Э���������ת�����protocol/transformer
	static File zipFile;
	static String zipFileName;
	public ExceptionHandler() {
		// TODO �Զ����ɵĹ��캯�����
	}
	public ExceptionHandler(String projectPath,String zipPath,String isRemoteDeploy,String remoteUrl,int port,String connectorType) {
		// TODO �Զ����ɵĹ��캯�����
		this.projectPath = projectPath;
		this.zipPath = zipPath;
		this.isRemoteDeploy = isRemoteDeploy;
		this.remoteUrl = remoteUrl;
		this.port = port;
		this.connectorType = connectorType;
		this.zipFile = new File(zipPath);
		String FileName = zipFile.getName();
		this.zipFileName = FileName.substring(0, FileName.lastIndexOf("."));
	}
	
	public void createProtocolProperty(){
		//��ȡprotocol.properties��ѹ����zip�ļ�
		Protocol protocol = new Protocol(projectPath,zipFile.getParentFile().getAbsolutePath(),zipFileName);
		protocol.getProcotolProperty();
				
			
	}
	
	public void createProcessorProperty(){
		//��ȡprotocol.properties��ѹ����zip�ļ�
		Processor processor = new Processor(projectPath,zipFile.getParentFile().getAbsolutePath(),zipFileName);
		processor.getProcessorProperty();
	}
	
	public void remoteDeploy(){
		//Զ�̲���
		LogFile log = new LogFile();
		if(isRemoteDeploy.equals("true")){
			try {
				log.appendLog("��ʼԶ�̲���");
				log.appendLog("Զ�̷�����IP��" + remoteUrl);
				log.appendLog("Զ�̷������˿ڣ�" + port);
				Socket  client = new Socket(remoteUrl, port);
				PrintStream ps = new PrintStream(client.getOutputStream());
				String line = null;
				ps.println(zipFile.getName());
				ps.println(connectorType);
				log.appendLog("������Zip����" + zipFile.getAbsolutePath());
				log.appendLog("��������Ϊ��" + connectorType);
				
				OutputStream os = client.getOutputStream();
				FileInputStream fis = new FileInputStream(zipFile);
				byte[] b = new byte[1024];
				int length = 0;
				length = 0;
				log.appendLog("��ʼTCP����"+zipFile.getName()+"�ļ�");
				while((length = fis.read(b))!=-1){
//							System.out.println("length="+length);
					//���ļ�д��socket�����
					os.write(b,0,length);
				}
				os.close();
				ps.close();
				fis.close();
				log.appendLog("����"+zipFile.getName()+"�ļ�����");
				log.appendLog("*****************Զ�̲���ɹ�******************");
				log.appendLog("  ");
			} catch (UnknownHostException e) {
				// TODO �Զ����ɵ� catch ��
				log.appendLog("*****************Զ�̲���ʧ��******************");
				log.appendLog("  ");
				e.printStackTrace();
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				log.appendLog("*****************Զ�̲���ʧ��******************");
				log.appendLog("  ");
				e.printStackTrace();
			}
		}
		else{			
			log.appendLog("*****************���ز������******************");
			log.appendLog("  ");
		}
	}
}
