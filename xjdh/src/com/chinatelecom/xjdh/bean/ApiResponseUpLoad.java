package com.chinatelecom.xjdh.bean;

@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponseUpLoad {
	@org.codehaus.jackson.annotate.JsonProperty("ret")
	private java.lang.Integer ret;
	
	@org.codehaus.jackson.annotate.JsonProperty("data")
	private java.lang.String data;
	@org.codehaus.jackson.annotate.JsonProperty("newGrouping")
	private java.lang.String newGrouping;

	public java.lang.Integer getRet() {
		return ret;
	}

	public void setRet(java.lang.Integer ret) {
		this.ret = ret;
	}

	public java.lang.String getData() {
		return data;
	}

	public void setData(java.lang.String data) {
		this.data = data;
	}

	public java.lang.String getNewGrouping() {
		return newGrouping;
	}

	public void setNewGrouping(java.lang.String newGrouping) {
		this.newGrouping = newGrouping;
	}
	
}
