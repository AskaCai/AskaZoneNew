package org.mule.tooling.devkit.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;



/**
 * @author csd
 *���ն�
 */
public class FileServer {
	private static int LISTEN_PORT;
	private static String SOURCE_PATH;
	private static String IMAGE_PATH;
	
	public static void main(String[] args) {
		// TODO �Զ����ɵķ������
		try {
			ServerSocket server = new ServerSocket(9100);
			Socket socket = new Socket();
			InputStream is ;
			OutputStream os ;
			while(true){ //��ÿ������zip��������һ���̴߳���
				socket = server.accept();
				new Thread(new ServerThread(socket)).run();
//				is = socket.getInputStream();
//				os = socket.getOutputStream();
//				byte[] b = new byte[1024];
//				
//
//				//�õ��ļ���������
//				int a = is.read(b);
//				String receiveMessage = new String(b,0,a);
//				String fileName = receiveMessage.substring(0,receiveMessage.indexOf("&"));
//				String compnentType = receiveMessage.substring(receiveMessage.indexOf("&")+1,receiveMessage.length());
//				
//				System.out.println("���ܵ����ļ���Ϊ��" + fileName);
//				System.out.println("���ܵ�������Ϊ��" + compnentType);
//				
//				String suffix = fileName.substring(fileName.indexOf("."),fileName.length());
//				DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
//				String nn = df.format(new Date());
//				fileName = fileName.substring(0,fileName.indexOf(".zip"))+"-"+nn+suffix;
//				System.out.println("�����ɵ��ļ���Ϊ��" + fileName);
//				FileOutputStream fos = new FileOutputStream("D:\\csd\\try"+ File.separator + fileName);
//				int length = 0;
//				while((length =is.read(b))!=-1){
//					//��socket������д���ļ��������
//					fos.write(b, 0, length);
//				}
//				//fos.flush
//				fos.close();
//				os.flush();
//				
//				is.close();
//				os.close();
//				break;
			}
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		
	}
	



}
