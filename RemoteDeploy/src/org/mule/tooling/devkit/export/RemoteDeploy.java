package org.mule.tooling.devkit.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import net.lingala.zip4j.core.ZipFile;

import org.mule.tooling.devkit.Processor.Processor;
import org.mule.tooling.devkit.Protocol.Protocol;
import org.mule.tooling.devkit.Transformer.Transformer;
import org.mule.tooling.devkit.log.LogFile;
import org.mule.tooling.devkit.zip.ZipUtils;

/**
 * @author Csd
 * ���Ͷ�
 */
public class RemoteDeploy {
	public static void main(String[] args) {
		LogFile log = new LogFile();
		log.appendLog("���뵽RemoteDeploy.jar��main����");
		log.appendLog("*****************����ʼ******************");
		String projectPath = args[0];
		log.appendLog("Ҫ��������Ŀ·����" + projectPath);
		String zipPath = args[1];
		log.appendLog("������zip��·����" + zipPath);
		String isRemoteDeploy = args[2];
		log.appendLog("�Ƿ�Զ�̲���" + isRemoteDeploy);
		String remoteUrl = args[3];
		log.appendLog("Զ�̲���ip��" + remoteUrl);
		int port = Integer.parseInt(args[4]);
		log.appendLog("Զ�̲���˿ڣ�" + port);
		String connectorType = args[5]; //Э���������ת�����protocol/transformer
		log.appendLog("����������ͣ�" + connectorType);
		File zipFile = null;
		new ExceptionHandler(projectPath, zipPath, isRemoteDeploy, remoteUrl, port, connectorType);//��ʼ���쳣������
		
//		String zipPath = "C:\\Users\\vmuser\\Desktop\\�½��ļ���\\one\\27.0\\one.zip";
//		String remoteUrl = "192.170.20.225";
//		int port = 9100;
//		String connectorType = "protocol";
		zipFile = new File(zipPath);
		String zipFileName = zipFile.getName();
		zipFileName = zipFileName.substring(0, zipFileName.lastIndexOf("."));
		
		//��ȡtransformer.properties��ѹ����zip�ļ�
		Transformer transformer = new Transformer(projectPath,zipFile.getParentFile().getAbsolutePath(),zipFileName);
		transformer.getTransformerProperty();
		
		//��ȡprotocol.properties��ѹ����zip�ļ�
		Protocol protocol = new Protocol(projectPath,zipFile.getParentFile().getAbsolutePath(),zipFileName);
		protocol.getProcotolProperty();
		
		//��ȡprotocol.properties��ѹ����zip�ļ�
		Processor processor = new Processor(projectPath,zipFile.getParentFile().getAbsolutePath(),zipFileName);
		processor.getProcessorProperty();
		
		//Զ�̲���
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
//					System.out.println("length="+length);
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
