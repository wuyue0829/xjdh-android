package com.chinatelecom.xjdh.utils;

import java.util.HashMap;
import java.util.Map;

public class SharedConst {
	public static String CLIENT_PASSWORD = "036D45B52D2FDDBC12A171015947CE71";
	public static String PASSWORD_CRYPRO_SEED = "xjtele";
	public static String CLIENT_ID = "xjtele";
	public static String CLIENT_REDIRECT_URL = "xjdh.jimglobal.com";
	public static String HTTP_AUTHORIZATION = "Authorization";
	public static String FILE_AREA_JSON = "area.json";
	public static String FILE_MODEL_JSON = "model.json";
	public static String USER_IMAGE = "user_image";
	public static int DEFAULT_PAGE_SIZE = 10;
	public static Map<String, String> DEV_MODEL_MAP = new HashMap<String, String>() {
		private static final long serialVersionUID = -1098171620713871607L;

		{
			put("water", "水浸");
			put("temperature", "温度");
			put("humid", "湿度");
			put("smoke", "烟感");
			put("imem_12", "智能电表");
			put("battery_24", "交直流屏电源蓄电池组");
			put("battery_32", "UPS电源蓄电池组");
			put("fresh_air", "新风系统");
			put("psma-ac", "PSM-A交流屏电源");
			put("psma-rc", "PSM-A整流屏电源");
			put("psma-dc", "PSM-A直流屏电源");
			put("Netsure801CA7", "801交直流屏电源");
			put("liebert-ups", "力博特UPS");
			put("liebert-ac", "力博特空调");
			put("motivator", "油机");
		}
	};
}
