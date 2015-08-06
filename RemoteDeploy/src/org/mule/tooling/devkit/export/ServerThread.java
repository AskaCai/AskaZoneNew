package org.mule.tooling.devkit.export;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerThread implements Runnable{
	
	Socket s = null;
	BufferedReader br = null;
	InputStream is =null;
	public ServerThread(Socket s) throws IOException{
		this.s = s;
		br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		is = s.getInputStream();
	}
	
	@Override
	public void run() {
		// TODO �Զ����ɵķ������
		String content = null;
		String fileName = readFromClient();
		String compnentType = readFromClient();
		System.out.println("���ܵ����ļ���Ϊ��" + fileName);
		System.out.println("���ܵ�������Ϊ��" + compnentType);
		
		String suffix = fileName.substring(fileName.indexOf("."),fileName.length());
		DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
		String nn = df.format(new Date());
		fileName = fileName.substring(0,fileName.indexOf(".zip"))+"-"+nn+suffix;
		System.out.println("�����ɵ��ļ���Ϊ��" + fileName);
		
		
		try {
			byte[] b = new byte[1024];
			FileOutputStream fos = new FileOutputStream("E:\\���Ժ\\try\\FileServerLocation"+ File.separator + fileName);
			int length = 0;
			while((length =is.read(b))!=-1){
				//��socket������д���ļ��������
				fos.write(b, 0, length);
			}
			//fos.flush
			fos.close();
			is.close();
			br.close();
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}	
	}

	private String readFromClient() {
		// TODO �Զ����ɵķ������
		try {
			return br.readLine();
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		return null;
		
	}
	
}
