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
	static String connectorType; //协议组件还是转换组件protocol/transformer
	static File zipFile;
	static String zipFileName;
	public ExceptionHandler() {
		// TODO 自动生成的构造函数存根
	}
	public ExceptionHandler(String projectPath,String zipPath,String isRemoteDeploy,String remoteUrl,int port,String connectorType) {
		// TODO 自动生成的构造函数存根
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
		//获取protocol.properties并压缩进zip文件
		Protocol protocol = new Protocol(projectPath,zipFile.getParentFile().getAbsolutePath(),zipFileName);
		protocol.getProcotolProperty();
				
			
	}
	
	public void createProcessorProperty(){
		//获取protocol.properties并压缩进zip文件
		Processor processor = new Processor(projectPath,zipFile.getParentFile().getAbsolutePath(),zipFileName);
		processor.getProcessorProperty();
	}
	
	public void remoteDeploy(){
		//远程部署
		LogFile log = new LogFile();
		if(isRemoteDeploy.equals("true")){
			try {
				log.appendLog("开始远程部署");
				log.appendLog("远程服务器IP：" + remoteUrl);
				log.appendLog("远程服务器端口：" + port);
				Socket  client = new Socket(remoteUrl, port);
				PrintStream ps = new PrintStream(client.getOutputStream());
				String line = null;
				ps.println(zipFile.getName());
				ps.println(connectorType);
				log.appendLog("欲部署Zip包：" + zipFile.getAbsolutePath());
				log.appendLog("部署类型为：" + connectorType);
				
				OutputStream os = client.getOutputStream();
				FileInputStream fis = new FileInputStream(zipFile);
				byte[] b = new byte[1024];
				int length = 0;
				length = 0;
				log.appendLog("开始TCP传输"+zipFile.getName()+"文件");
				while((length = fis.read(b))!=-1){
//							System.out.println("length="+length);
					//把文件写入socket输出流
					os.write(b,0,length);
				}
				os.close();
				ps.close();
				fis.close();
				log.appendLog("传输"+zipFile.getName()+"文件结束");
				log.appendLog("*****************远程部署成功******************");
				log.appendLog("  ");
			} catch (UnknownHostException e) {
				// TODO 自动生成的 catch 块
				log.appendLog("*****************远程部署失败******************");
				log.appendLog("  ");
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				log.appendLog("*****************远程部署失败******************");
				log.appendLog("  ");
				e.printStackTrace();
			}
		}
		else{			
			log.appendLog("*****************本地部署结束******************");
			log.appendLog("  ");
		}
	}
}
