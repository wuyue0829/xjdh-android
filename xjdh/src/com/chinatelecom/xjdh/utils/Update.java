package com.chinatelecom.xjdh.utils;

import java.io.Serializable;

/**
 * 应用程序更新实体类
 * 
 * @author peter
 * @version 1.1
 * @created 2015-07-08
 */
public class Update implements Serializable {
	private static final long serialVersionUID = -4181397418247151521L;
	@org.codehaus.jackson.annotate.JsonProperty("id")
	private java.lang.Integer id;
	@org.codehaus.jackson.annotate.JsonProperty("version_code")
	private java.lang.Integer versionCode;
	@org.codehaus.jackson.annotate.JsonProperty("version_name")
	private java.lang.String versionName;
	@org.codehaus.jackson.annotate.JsonProperty("download_url")
	private java.lang.String downloadUrl;
	@org.codehaus.jackson.annotate.JsonProperty("update_log")
	private java.lang.String updateLog;
	@org.codehaus.jackson.annotate.JsonProperty("update_datetime")
	private java.lang.String updateDatetime;

	public java.lang.String getUpdateDatetime() {
		return updateDatetime;
	}

	public void setUpdateDatetime(java.lang.String updateDatetime) {
		this.updateDatetime = updateDatetime;
	}

	public java.lang.Integer getId() {
		return id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	public java.lang.Integer getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(java.lang.Integer versionCode) {
		this.versionCode = versionCode;
	}

	public java.lang.String getVersionName() {
		return versionName;
	}

	public void setVersionName(java.lang.String versionName) {
		this.versionName = versionName;
	}

	public java.lang.String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(java.lang.String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public java.lang.String getUpdateLog() {
		return updateLog;
	}

	public void setUpdateLog(java.lang.String updateLog) {
		this.updateLog = updateLog;
	}
}
