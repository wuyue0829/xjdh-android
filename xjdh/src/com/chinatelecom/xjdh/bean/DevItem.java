/* Generated by JavaFromJSON */
/*http://javafromjson.dashingrocket.com*/

package com.chinatelecom.xjdh.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class DevItem implements Parcelable {

	@org.codehaus.jackson.annotate.JsonProperty("model")
	private java.lang.String model;

	public void setModel(java.lang.String model) {
		this.model = model;
	}

	public java.lang.String getModel() {
		return model;
	}

	@org.codehaus.jackson.annotate.JsonProperty("name")
	private java.lang.String name;

	public void setName(java.lang.String name) {
		this.name = name;
	}

	public java.lang.String getName() {
		return name;
	}

	@org.codehaus.jackson.annotate.JsonProperty("data_id")
	private java.lang.String data_id;

	public void setData_id(java.lang.String data_id) {
		this.data_id = data_id;
	}

	public java.lang.String getData_id() {
		return data_id;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(model);
		out.writeString(name);
		out.writeString(data_id);
	}

	public static final Parcelable.Creator<DevItem> CREATOR = new Creator<DevItem>() {
		@Override
		public DevItem[] newArray(int size) {
			return new DevItem[size];
		}

		@Override
		public DevItem createFromParcel(Parcel in) {
			return new DevItem(in);
		}
	};

	public DevItem() {
		super();
	}

	public DevItem(Parcel in) {
		model = in.readString();
		name = in.readString();
		data_id = in.readString();
	}
}