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
 * 发送端
 */
public class RemoteDeploy {
	public static void main(String[] args) {
		LogFile log = new LogFile();
		log.appendLog("进入到RemoteDeploy.jar的main函数");
		log.appendLog("*****************部署开始******************");
		String projectPath = args[0];
		log.appendLog("要导出的项目路径：" + projectPath);
		String zipPath = args[1];
		log.appendLog("导出的zip包路径：" + zipPath);
		String isRemoteDeploy = args[2];
		log.appendLog("是否远程部署：" + isRemoteDeploy);
		String remoteUrl = args[3];
		log.appendLog("远程部署ip：" + remoteUrl);
		int port = Integer.parseInt(args[4]);
		log.appendLog("远程部署端口：" + port);
		String connectorType = args[5]; //协议组件还是转换组件protocol/transformer
		log.appendLog("组件部署类型：" + connectorType);
		File zipFile = null;
		new ExceptionHandler(projectPath, zipPath, isRemoteDeploy, remoteUrl, port, connectorType);//初始化异常处理类
		
//		String zipPath = "C:\\Users\\vmuser\\Desktop\\新建文件夹\\one\\27.0\\one.zip";
//		String remoteUrl = "192.170.20.225";
//		int port = 9100;
//		String connectorType = "protocol";
		zipFile = new File(zipPath);
		String zipFileName = zipFile.getName();
		zipFileName = zipFileName.substring(0, zipFileName.lastIndexOf("."));
		
		//获取transformer.properties并压缩进zip文件
		Transformer transformer = new Transformer(projectPath,zipFile.getParentFile().getAbsolutePath(),zipFileName);
		transformer.getTransformerProperty();
		
		//获取protocol.properties并压缩进zip文件
		Protocol protocol = new Protocol(projectPath,zipFile.getParentFile().getAbsolutePath(),zipFileName);
		protocol.getProcotolProperty();
		
		//获取protocol.properties并压缩进zip文件
		Processor processor = new Processor(projectPath,zipFile.getParentFile().getAbsolutePath(),zipFileName);
		processor.getProcessorProperty();
		
		//远程部署
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
//					System.out.println("length="+length);
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
