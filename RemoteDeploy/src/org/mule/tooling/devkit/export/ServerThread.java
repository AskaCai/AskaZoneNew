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
		// TODO 自动生成的方法存根
		String content = null;
		String fileName = readFromClient();
		String compnentType = readFromClient();
		System.out.println("接受到的文件名为：" + fileName);
		System.out.println("接受到的类型为：" + compnentType);
		
		String suffix = fileName.substring(fileName.indexOf("."),fileName.length());
		DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
		String nn = df.format(new Date());
		fileName = fileName.substring(0,fileName.indexOf(".zip"))+"-"+nn+suffix;
		System.out.println("新生成的文件名为：" + fileName);
		
		
		try {
			byte[] b = new byte[1024];
			FileOutputStream fos = new FileOutputStream("E:\\电科院\\try\\FileServerLocation"+ File.separator + fileName);
			int length = 0;
			while((length =is.read(b))!=-1){
				//把socket输入流写到文件输出流中
				fos.write(b, 0, length);
			}
			//fos.flush
			fos.close();
			is.close();
			br.close();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}	
	}

	private String readFromClient() {
		// TODO 自动生成的方法存根
		try {
			return br.readLine();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return null;
		
	}
	
}
