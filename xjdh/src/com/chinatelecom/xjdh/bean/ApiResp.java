package com.chinatelecom.xjdh.bean;
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResp {
	@org.codehaus.jackson.annotate.JsonProperty("ret")
	private java.lang.Integer ret;
	
	@org.codehaus.jackson.annotate.JsonProperty("url")
	private java.lang.String data;

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

	@Override
	public String toString() {
		return "ApiResp [ret=" + ret + ", data=" + data + "]";
	}
	
}
