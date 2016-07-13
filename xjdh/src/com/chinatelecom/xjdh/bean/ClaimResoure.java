package com.chinatelecom.xjdh.bean;

import java.io.Serializable;

/**
 * 申请资源
 * 
 * @author admin
 *
 */
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class ClaimResoure implements Serializable {
	// {"error":"0","account":"13966666666","username":"aaa","deptname":"中国电信","email
	// ":"xxx@sina.com","telephone":"1234567","deptid":"11","mobile":"13966666666"}
	@org.codehaus.jackson.annotate.JsonProperty("error") // 错误码，等于0表示请求成功
	private java.lang.String error;
	@org.codehaus.jackson.annotate.JsonProperty("account") // 用户账号,目前仅支持手机号。
	private java.lang.String account;
	@org.codehaus.jackson.annotate.JsonProperty("username") // 用户的姓名
	private java.lang.String username;
	@org.codehaus.jackson.annotate.JsonProperty("deptname") // 用户的组织机构
	private java.lang.String deptname;
	@org.codehaus.jackson.annotate.JsonProperty("email") // 用户的邮箱
	private java.lang.String email;
	@org.codehaus.jackson.annotate.JsonProperty("telephone") // 用户的电话
	private java.lang.String telephone;
	@org.codehaus.jackson.annotate.JsonProperty("deptid") // 用户的组织机构id
	private java.lang.String deptid;
	@org.codehaus.jackson.annotate.JsonProperty("mobile") // 用户的手机号
	private java.lang.String mobile;

	@org.codehaus.jackson.annotate.JsonProperty("error_description")
	private String error_description;

	public String getError_description() {
		return error_description;
	}

	public void setError_description(String error_description) {
		this.error_description = error_description;
	}

	public java.lang.String getError() {
		return error;
	}

	public void setError(java.lang.String error) {
		this.error = error;
	}

	public java.lang.String getAccount() {
		return account;
	}

	public void setAccount(java.lang.String account) {
		this.account = account;
	}

	public java.lang.String getUsername() {
		return username;
	}

	public void setUsername(java.lang.String username) {
		this.username = username;
	}

	public java.lang.String getDeptname() {
		return deptname;
	}

	public void setDeptname(java.lang.String deptname) {
		this.deptname = deptname;
	}

	public java.lang.String getEmail() {
		return email;
	}

	public void setEmail(java.lang.String email) {
		this.email = email;
	}

	public java.lang.String getTelephone() {
		return telephone;
	}

	public void setTelephone(java.lang.String telephone) {
		this.telephone = telephone;
	}

	public java.lang.String getDeptid() {
		return deptid;
	}

	public void setDeptid(java.lang.String deptid) {
		this.deptid = deptid;
	}

	public java.lang.String getMobile() {
		return mobile;
	}

	public void setMobile(java.lang.String mobile) {
		this.mobile = mobile;
	}

}
