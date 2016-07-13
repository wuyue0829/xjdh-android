package com.chinatelecom.xjdh.bean;

import java.io.Serializable;

/**
 * 申请openId
 * 
 * @author admin
 *
 */
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class ClaimOpenId implements Serializable {
	// {"error":"0","client_id":"jj","open_id":"CTINM_B983FFD82CEB721481A5A6EB7A16036D"}
	@org.codehaus.jackson.annotate.JsonProperty("error")
	private java.lang.String error;
	@org.codehaus.jackson.annotate.JsonProperty("client_id")
	private java.lang.String client_id;
	@org.codehaus.jackson.annotate.JsonProperty("open_id")
	private java.lang.String open_id;
	@org.codehaus.jackson.annotate.JsonProperty("error_description")
	private String error_description;
	public java.lang.String getError() {
		return error;
	}

	public void setError(java.lang.String error) {
		this.error = error;
	}

	public java.lang.String getClient_id() {
		return client_id;
	}

	public void setClient_id(java.lang.String client_id) {
		this.client_id = client_id;
	}

	public java.lang.String getOpen_id() {
		return open_id;
	}

	public void setOpen_id(java.lang.String open_id) {
		this.open_id = open_id;
	}

	public String getError_description() {
		return error_description;
	}

	public void setError_description(String error_description) {
		this.error_description = error_description;
	}

}
