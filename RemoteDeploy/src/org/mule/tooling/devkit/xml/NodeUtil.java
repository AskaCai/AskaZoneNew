package org.mule.tooling.devkit.xml;

import org.mule.tooling.devkit.log.LogFile;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NodeUtil {
	public static String getGlobalParameter(NodeList list){
		Node node =null;
		StringBuilder gobalParameter = new StringBuilder("[");;
		String gobalArgName = null;
		String defaultValue = null;
		String javaType =null;
		
		for(int i = 0; i < list.getLength(); i++){
			node = list.item(i);
			if(node.getAttributes().getNamedItem("name")!=null){
				gobalArgName = node.getAttributes().getNamedItem("name").getNodeValue();
			}else{
				gobalArgName = "";
			}
			if(node.getAttributes().getNamedItem("defaultValue")!=null){
				defaultValue = node.getAttributes().getNamedItem("defaultValue").getNodeValue();
			}else{
				defaultValue = "";
			}
			if(node.getAttributes().getNamedItem("javaType")!=null){
				javaType = node.getAttributes().getNamedItem("javaType").getNodeValue();
			}else{
				javaType = "";
			}
			gobalParameter.append("{\"name\":\"" + gobalArgName + "\",\"type\":\"input\",\"value\":\"" + defaultValue +"\"}");
			if(i!=list.getLength()-1){
				gobalParameter.append(",");
			}else{
				gobalParameter.append("]");
			}
			if(list.getLength() == 0){
				gobalParameter.append("]");
			}
		}
		return gobalParameter.toString();
	}
	
	public static Operation[] getOperationsParameter(EditorXml editorXml){
		Operation[] operations=null;
		Node node0 =null;
		String command =null;
		NodeList list =null;
		String[] operationNames = null;
		String json =null;
		
		command = "/namespace/cloud-connector-message-source/attribute-category/group[@caption='Basic Settings']/modeSwitch/mode";
		list = editorXml.getNodeList(command);
		if(list!=null& list.getLength()>0){
			operationNames = new String[list.getLength()];
			operations = new Operation[list.getLength()];
			for(int i = 0; i<list.getLength(); i++){
				operationNames[i] = list.item(i).getAttributes().getNamedItem("modeLabel").getNodeValue();
				operationNames[i] = operationNames[i].replace(' ', '-').toLowerCase();
			}
			for(int i = 0; i<operationNames.length; i++){
				command = "/namespace/cloud-connector-message-source[@inboundLocalName=\'"+operationNames[i]+"\']/attribute-category/group/*";
				list = editorXml.getNodeList(command);
				json = getGlobalParameter(list);
				Operation operation = new Operation();
				operation.setName(operationNames[i]);
				operation.setJson(json);
				operations[i] = operation;
			}
		}
		return operations;
	}
	public static Operation[] getProcessorOperationsParameter(EditorXml editorXml){
		Operation[] operations=null;
		Node node =null;
		String nodeVal =null ;
		String command =null;
		NodeList list =null;
		String[] operationNames = null;
		String[] returnTypes =null;
		String json =null;
		LogFile log = new LogFile();
		
		command = "/namespace/cloud-connector/attribute-category/group[@caption='Basic Settings']/modeSwitch/mode";
		log.appendLog("command:"+command);
		list = editorXml.getNodeList(command);
		if(list!=null& list.getLength()>0){
			operationNames = new String[list.getLength()];
			operations = new Operation[list.getLength()];
			returnTypes = new String[list.getLength()];
			for(int i = 0; i<list.getLength(); i++){
				operationNames[i] = list.item(i).getAttributes().getNamedItem("modeLabel").getNodeValue();
				//需要将每个单词的首字母大写
				operationNames[i] = firstWordToUpper(operationNames[i]);
			}
			//获取返回类型
			for(int i = 0; i<operationNames.length; i++){
				command = "/namespace/cloud-connector[@caption=\'"+operationNames[i]+"\']";
				log.appendLog("command:"+command);
				list = editorXml.getNodeList(command);
				log.appendLog("此command从editor.xml中获取到的节点数为："+list.getLength());
				node = list.item(0);
				nodeVal =node.getAttributes().getNamedItem("returnType").getNodeValue();
				returnTypes[i] = nodeVal;
			}
			//获取参数并将所有信息写入operation的list当中
			for(int i = 0; i<operationNames.length; i++){
				command = "/namespace/cloud-connector[@caption=\'"+operationNames[i]+"\']/attribute-category[@caption='General']/group/*";
				list = editorXml.getNodeList(command);
				json = getGlobalParameter(list);
				Operation operation = new Operation();
				operation.setName(operationNames[i].replace(' ', '-').toLowerCase());
				operation.setJson(json);
				operation.setReturnType(returnTypes[i]);
				operations[i] = operation;
			}
		}
		
		return operations;
	}
	
	public static String firstWordToUpper(String original){
		StringBuilder finalResult = new StringBuilder();
		String[] words;
		words = original.split(" ");
		for (int i = 0; i < words.length; i++) {
			finalResult.append(Character.toUpperCase(words[i].charAt(0))).append(words[i].substring(1));
			if(i < words.length-1){
				finalResult.append(" ");
			}
		}
		return finalResult.toString();
	}
}
