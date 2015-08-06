package org.mule.tooling.devkit.xml;

public class Operation {
	private String name ;
	private String Json ;
	private String returnType;//只有processor需要
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getJson() {
		return Json;
	}
	public void setJson(String json) {
		Json = json;
	}
	public String getReturnType() {
		return returnType;
	}
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	
}
