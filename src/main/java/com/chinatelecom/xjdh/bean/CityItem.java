/* Generated by JavaFromJSON */
/*http://javafromjson.dashingrocket.com*/

package com.chinatelecom.xjdh.bean;
/**
 * @author peter
 * 
 */
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class CityItem {
	@org.codehaus.jackson.annotate.JsonProperty("name")
	private String name;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@org.codehaus.jackson.annotate.JsonProperty("city_code")
	private String city_code;

	public void setCity_code(String city_code) {
		this.city_code = city_code;
	}

	public String getCity_code() {
		return city_code;
	}

	@org.codehaus.jackson.annotate.JsonProperty("countyList")
	private CountyItem[] countylist;

	public void setCountylist(CountyItem[] countylist) {
		this.countylist = countylist;
	}

	public CountyItem[] getCountylist() {
		return countylist;
	}

}