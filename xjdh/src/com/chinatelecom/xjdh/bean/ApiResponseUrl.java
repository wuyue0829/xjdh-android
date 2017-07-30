package com.chinatelecom.xjdh.bean;

public class ApiResponseUrl {
	@org.codehaus.jackson.annotate.JsonProperty("ret")
	private java.lang.Integer ret;
	
	@org.codehaus.jackson.annotate.JsonProperty("rtsp_url")
	private java.lang.String rtsp_url;

	public java.lang.Integer getRet() {
		return ret;
	}

	public void setRet(java.lang.Integer ret) {
		this.ret = ret;
	}

	public java.lang.String getRtsp_url() {
		return rtsp_url;
	}

	public void setRtsp_url(java.lang.String rtsp_url) {
		this.rtsp_url = rtsp_url;
	}

	@Override
	public String toString() {
		return "ApiResponseUrl [ret=" + ret + ", rtsp_url=" + rtsp_url + "]";
	}
	
	
}
