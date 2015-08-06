package org.mule.tooling.devkit.Transformer;


import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Transformer;
import org.mule.tooling.devkit.Transformer.ConnectorInfo;
import org.mule.tooling.devkit.Transformer.FileUtil;
import org.mule.tooling.devkit.export.ExceptionHandler;
import org.mule.tooling.devkit.log.LogFile;


public class TransformerAnnotation {
	String projectPath = null;
	String classPath = null;
	Map<String, Object> types = new HashMap<String, Object>();
	LogFile log;
	public TransformerAnnotation(String projectPath) {
		// TODO 自动生成的构造函数存根
		this.projectPath = projectPath;
		log = new LogFile();
	}
	public Map<String, Object> getTypes() {
		classPath = new ConnectorInfo(projectPath).getConnectorPath();
		log.appendLog(classPath);
		log.appendLog("开始类加载,根路径为"+projectPath+File.separator+"target"+File.separator+"classes");
		try {
			classLoaderClasses(projectPath+File.separator+"target"+File.separator+"classes","classes");
			log.appendLog("类加载完成");
			Method[] methods = Class.forName(classPath).getMethods();
			log.appendLog("已将"+classPath+"调入JVM");
			String[] tMethods=null ; 
			Class[][] sourceTypes = new Class[methods.length][];
			Class[] returntype = new Class[methods.length];
			String[] methodName = new String[methods.length];
			int i=0;
			//****************若一个Transformer要多个方法，则这里可能要用xml
//			System.out.println(classPath);
			
			for(Method m : methods){
				Transformer meta = m.getAnnotation(Transformer.class);
				if(meta!= null){
					sourceTypes[i] = meta.sourceTypes();
					returntype[i] = m.getReturnType();
					methodName[i] = m.getName();
					i++;
				}
			}
			types.put("SourceTypes", sourceTypes);
			types.put("ReturnType", returntype);
			types.put("Methods", methodName);
			types.put("MethodNum",i);
		
//			Connector cmeta = (Connector) Class.forName(classPath).getAnnotation(Connector.class);
//			System.out.println("ConnectorName = "+cmeta.name());
//			if(cmeta!=null)
//				types.put("ConnectorName", cmeta.name());
		} catch (SecurityException e) {
			// TODO 自动生成的 catch 块
			log.appendLog("SecurityException,放弃生成transformre.properties!!");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO 自动生成的 catch 块
			log.appendLog("ClassNotFoundException,放弃生成transformre.properties!!");
			log.appendLog("进行异常处理，继续生成protocol和processor的properties文件");
			ExceptionHandler eh = new  ExceptionHandler();
			eh.createProtocolProperty();
			eh.createProcessorProperty();
			eh.remoteDeploy();
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			log.appendLog("IOException,放弃生成transformre.properties!!");
			e.printStackTrace();
		} 
		return types;
	}
	
	public void classLoaderClasses( String parentPath, String fileName) throws  IOException {
		// 设置class文件所在根路径
		// 例如/usr/java/classes下有一个test.App类，则/usr/java/classes即这个类的根路径，而.class文件的实际位置是/usr/java/classes/test/App.class
		File classPath = new File(parentPath);
		if(classPath.exists()) {
			// 记录加载.class文件的数量
			if (classPath.exists() && classPath.isDirectory()) {
				// 获取路径长度
				Stack<File> stack = new Stack<>();
				stack.push(classPath);
	
				// 遍历类路径
				while (stack.isEmpty() == false) {
					File path = stack.pop();
					File[] classFiles = path.listFiles(new FileFilter() {
						public boolean accept(File pathname) {
							return pathname.isDirectory() || pathname.getName().endsWith(".class");
						}
					});
					if(classFiles.length > 0) {
						for (File subFile : classFiles) {
							if (subFile.isDirectory()) {
								stack.push(subFile);
							} 
							else {
//								if (classCount++ == 0) {
									Method method =null;
									try {
										method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
									} catch (NoSuchMethodException e1) {
										// TODO 自动生成的 catch 块
										e1.printStackTrace();
									} catch (SecurityException e1) {
										// TODO 自动生成的 catch 块
										e1.printStackTrace();
									}
									boolean accessible = method.isAccessible();
									try {
										if (accessible == false) {
											method.setAccessible(true);
										}
										// 设置类加载器
										URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
										// 将当前类路径加入到类加载器中
										try {
											method.invoke(classLoader, classPath.toURI().toURL());
										} catch (IllegalAccessException e) {
											// TODO 自动生成的 catch 块
											e.printStackTrace();
										} catch (IllegalArgumentException e) {
											// TODO 自动生成的 catch 块
											e.printStackTrace();
										} catch (InvocationTargetException e) {
											// TODO 自动生成的 catch 块
											log.appendLog("InvocationTargetException,放弃生成transformre.properties!!");
											log.appendLog("进行异常处理，继续生成protocol和processor的properties文件");
											ExceptionHandler eh = new  ExceptionHandler();
											eh.createProtocolProperty();
											eh.createProcessorProperty();
											eh.remoteDeploy();
											e.printStackTrace();
										}

									} finally {
										method.setAccessible(accessible);
									}

							}
						}
					}

				}
			}
		}
	}
	public String getProjectPath() {
		return projectPath;
	}
	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}
	public String getClassPath() {
		return classPath;
	}
	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}
	
}
