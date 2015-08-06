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
		// TODO �Զ����ɵĹ��캯�����
		this.projectPath = projectPath;
		log = new LogFile();
	}
	public Map<String, Object> getTypes() {
		classPath = new ConnectorInfo(projectPath).getConnectorPath();
		log.appendLog(classPath);
		log.appendLog("��ʼ�����,��·��Ϊ"+projectPath+File.separator+"target"+File.separator+"classes");
		try {
			classLoaderClasses(projectPath+File.separator+"target"+File.separator+"classes","classes");
			log.appendLog("��������");
			Method[] methods = Class.forName(classPath).getMethods();
			log.appendLog("�ѽ�"+classPath+"����JVM");
			String[] tMethods=null ; 
			Class[][] sourceTypes = new Class[methods.length][];
			Class[] returntype = new Class[methods.length];
			String[] methodName = new String[methods.length];
			int i=0;
			//****************��һ��TransformerҪ������������������Ҫ��xml
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
			// TODO �Զ����ɵ� catch ��
			log.appendLog("SecurityException,��������transformre.properties!!");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO �Զ����ɵ� catch ��
			log.appendLog("ClassNotFoundException,��������transformre.properties!!");
			log.appendLog("�����쳣������������protocol��processor��properties�ļ�");
			ExceptionHandler eh = new  ExceptionHandler();
			eh.createProtocolProperty();
			eh.createProcessorProperty();
			eh.remoteDeploy();
			e.printStackTrace();
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			log.appendLog("IOException,��������transformre.properties!!");
			e.printStackTrace();
		} 
		return types;
	}
	
	public void classLoaderClasses( String parentPath, String fileName) throws  IOException {
		// ����class�ļ����ڸ�·��
		// ����/usr/java/classes����һ��test.App�࣬��/usr/java/classes�������ĸ�·������.class�ļ���ʵ��λ����/usr/java/classes/test/App.class
		File classPath = new File(parentPath);
		if(classPath.exists()) {
			// ��¼����.class�ļ�������
			if (classPath.exists() && classPath.isDirectory()) {
				// ��ȡ·������
				Stack<File> stack = new Stack<>();
				stack.push(classPath);
	
				// ������·��
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
										// TODO �Զ����ɵ� catch ��
										e1.printStackTrace();
									} catch (SecurityException e1) {
										// TODO �Զ����ɵ� catch ��
										e1.printStackTrace();
									}
									boolean accessible = method.isAccessible();
									try {
										if (accessible == false) {
											method.setAccessible(true);
										}
										// �����������
										URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
										// ����ǰ��·�����뵽���������
										try {
											method.invoke(classLoader, classPath.toURI().toURL());
										} catch (IllegalAccessException e) {
											// TODO �Զ����ɵ� catch ��
											e.printStackTrace();
										} catch (IllegalArgumentException e) {
											// TODO �Զ����ɵ� catch ��
											e.printStackTrace();
										} catch (InvocationTargetException e) {
											// TODO �Զ����ɵ� catch ��
											log.appendLog("InvocationTargetException,��������transformre.properties!!");
											log.appendLog("�����쳣������������protocol��processor��properties�ļ�");
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
