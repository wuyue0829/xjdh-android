/* Generated by JavaFromJSON */
/*http://javafromjson.dashingrocket.com*/

package com.chinatelecom.xjdh.bean;

public class MessageItem {
	@org.codehaus.jackson.annotate.JsonProperty("id")
	private java.lang.Integer id;
	@org.codehaus.jackson.annotate.JsonProperty("title")
	private java.lang.String title;
	@org.codehaus.jackson.annotate.JsonProperty("content")
	private java.lang.String content;

	@org.codehaus.jackson.annotate.JsonProperty("added_datetime")
	private java.lang.String added_datetime;

	@org.codehaus.jackson.annotate.JsonProperty("is_web")
	private java.lang.Boolean is_web;

	@org.codehaus.jackson.annotate.JsonProperty("send_by")
	private java.lang.String send_by;

	public java.lang.String getTitle() {
		return title;
	}

	public void setTitle(java.lang.String title) {
		this.title = title;
	}

	public java.lang.Integer getId() {
		return id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	public java.lang.String getAdded_datetime() {
		return added_datetime;
	}

	public void setAdded_datetime(java.lang.String added_datetime) {
		this.added_datetime = added_datetime;
	}

	public java.lang.String getContent() {
		return content;
	}

	public void setContent(java.lang.String content) {
		this.content = content;
	}

	public java.lang.String getSend_by() {
		return send_by;
	}

	public void setSend_by(java.lang.String send_by) {
		this.send_by = send_by;
	}

	public java.lang.Boolean Is_web() {
		return is_web;
	}

	public void setIs_web(java.lang.Boolean is_web) {
		this.is_web = is_web;
	}
}