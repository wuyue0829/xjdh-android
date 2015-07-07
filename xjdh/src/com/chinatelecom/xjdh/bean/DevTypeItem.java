/* Generated by JavaFromJSON */
/*http://javafromjson.dashingrocket.com*/

package com.chinatelecom.xjdh.bean;

import java.util.Arrays;

import android.os.Parcel;
import android.os.Parcelable;

public class DevTypeItem implements Parcelable {
	@org.codehaus.jackson.annotate.JsonProperty("devList")
	private DevItem[] devList;

	public void setDevist(DevItem[] devList) {
		this.devList = devList;
	}

	public DevItem[] getDevlist() {
		return devList;
	}

	@org.codehaus.jackson.annotate.JsonProperty("name")
	private java.lang.String name;

	public void setName(java.lang.String name) {
		this.name = name;
	}

	public java.lang.String getName() {
		return name;
	}

	@org.codehaus.jackson.annotate.JsonProperty("type")
	private java.lang.String type;

	public void setType(java.lang.String type) {
		this.type = type;
	}

	public java.lang.String getType() {
		return type;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(type);
		out.writeString(name);
		out.writeParcelableArray(devList, flags);
	}

	public static final Parcelable.Creator<DevTypeItem> CREATOR = new Creator<DevTypeItem>() {
		@Override
		public DevTypeItem[] newArray(int size) {
			return new DevTypeItem[size];
		}

		@Override
		public DevTypeItem createFromParcel(Parcel in) {
			return new DevTypeItem(in);
		}
	};

	public DevTypeItem() {
		super();
	}

	public DevTypeItem(Parcel in) {
		type = in.readString();
		name = in.readString();
		Parcelable[] parcelables = in.readParcelableArray(DevItem.class.getClassLoader());
		if (parcelables != null) {
			devList = Arrays.copyOf(parcelables, parcelables.length, DevItem[].class);
		}
	}
}