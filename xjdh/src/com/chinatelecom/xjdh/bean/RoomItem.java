/* Generated by JavaFromJSON */
/*http://javafromjson.dashingrocket.com*/

package com.chinatelecom.xjdh.bean;

import android.os.Parcel;
import android.os.Parcelable;
/**
 * @author peter
 * 
 */
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class RoomItem implements Parcelable {
	public RoomItem() {
		super();
	}

	@org.codehaus.jackson.annotate.JsonProperty("id")
	private java.lang.String id;

	public void setId(java.lang.String id) {
		this.id = id;
	}

	public java.lang.String getId() {
		return id;
	}

	@org.codehaus.jackson.annotate.JsonProperty("name")
	private java.lang.String name;

	public void setName(java.lang.String name) {
		this.name = name;
	}

	public java.lang.String getName() {
		return name;
	}

	@org.codehaus.jackson.annotate.JsonProperty("code")
	private java.lang.String code;

	public void setCode(java.lang.String code) {
		this.code = code;
	}

	public java.lang.String getCode() {
		return code;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@org.codehaus.jackson.annotate.JsonProperty("lng")
	private java.lang.Double lng;

	public java.lang.Double getLng() {
		return lng;
	}

	public void setLng(java.lang.Double lng) {
		this.lng = lng;
	}

	@org.codehaus.jackson.annotate.JsonProperty("lat")
	private java.lang.Double lat;

	public java.lang.Double getLat() {
		return lat;
	}

	public void setLat(java.lang.Double lat) {
		this.lat = lat;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(id);
		out.writeString(name);
		out.writeString(code);
		out.writeDouble(lng);
		out.writeDouble(lat);
	}

	public static final Parcelable.Creator<RoomItem> CREATOR = new Creator<RoomItem>() {
		@Override
		public RoomItem[] newArray(int size) {
			return new RoomItem[size];
		}

		@Override
		public RoomItem createFromParcel(Parcel in) {
			return new RoomItem(in);
		}
	};

	public RoomItem(Parcel in) {
		id = in.readString();
		name = in.readString();
		code = in.readString();
		lng = in.readDouble();
		lat = in.readDouble();
	}

}