package com.chinatelecom.xjdh.rest.client;

import java.util.LinkedHashMap;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.RequiresHeader;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.RestClientHeaders;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;

import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.bean.ApiResponseImage;
import com.chinatelecom.xjdh.bean.ApiResponseUpLoad;
import com.chinatelecom.xjdh.bean.DoorOperation;
import com.chinatelecom.xjdh.bean.SPDevResponse;
import com.chinatelecom.xjdh.rest.interceptor.HttpBasicAuthenticatorInterceptor;
import com.chinatelecom.xjdh.utils.URLs;
import com.chinatelecom.xjdh.utils.Update;

/**
 * @author peter
 * 
 */
@Rest(rootUrl = URLs.URL_API_HOST + "/api/" + URLs.API_VERSION, converters = { MappingJacksonHttpMessageConverter.class,
		StringHttpMessageConverter.class, MyFormHttpMessageConverter.class, MyStringHttpMessageConverter.class,
		ResourceHttpMessageConverter.class, FormHttpMessageConverter.class,
		ByteArrayHttpMessageConverter.class }, interceptors = { HttpBasicAuthenticatorInterceptor.class })
@RequiresHeader("Authorization")
public interface ApiRestClientInterface extends RestClientHeaders {
	@Get("/getuserinfo")
	ApiResponse getUserInfo() throws RestClientException;

	@Get("/getAlarmList?citycode={citycode}&countycode={countycode}&substationId={substationId}&roomId={roomId}&level={level}&model={model}&startdatetime={startdatetime}&enddatetime={enddatetime}&offset={offset}&count={count}&lastId={lastId}")
	ApiResponse getAlarmList(String citycode, String countycode, String substationId, String roomId, String level,
			String model, String startdatetime, String enddatetime, String offset, String count, String lastId)
					throws RestClientException;

	@Get("/getPreAlarmList?citycode={citycode}&countycode={countycode}&substationId={substationId}&roomId={roomId}&level={level}&model={model}&startdatetime={startdatetime}&enddatetime={enddatetime}&offset={offset}&count={count}&lastId={lastId}")
	ApiResponse getPreAlarmList(String citycode, String countycode, String substationId, String roomId, String level,
			String model, String startdatetime, String enddatetime, String offset, String count, String lastId)
					throws RestClientException;

	@Get("/getAreaData?cityName={cityName}&stationName={stationName}&stationNames={stationNames}")
	ApiResponse getAreaData(String cityName, String stationName,String stationNames) throws RestClientException;

	@Get("/getDevModelData")
	ApiResponse getDevModelData() throws RestClientException;

	@Get("/getLatestAlarmId")
	String getLatestAlarmId();

	@Get("/getRoomDeviceList?roomcode={roomcode}&devtype={devtype}")
	ApiResponse getRoomDeviceList(String roomcode, String devtype);

	@Post("/addfeedback")
	ApiResponse addFeedback(LinkedHashMap<String, String> items) throws RestClientException;

	@Get("/getAlarmChartsData")
	ApiResponse getAlarmChartsData() throws RestClientException;

	@Post("/modifyuserinfo")
	ApiResponse modifyUserInfo(LinkedHashMap<String, String> items) throws RestClientException;

	@Post("/modifyuserimage")
	@RequiresHeader({ "Authorization", "Content-Type" })
	ApiResponse modifyuserimage(MultiValueMap<String, Object> multiValueMap) throws RestClientException;

	@Get(URLs.UPDATE_VERSION)
	@RequiresHeader(value = {})
	Update getUpdateInfo() throws RestClientException;

	@Get("/getmessage?msgtype={msgType}")
	ApiResponse getMessage(String msgType) throws RestClientException;

	// 预告警处理
	@Post("/postPreAlarmList")
	ApiResponse postPreAlarmList(LinkedHashMap<String, String> lastd) throws RestClientException;

	// 上传图片
	@Post("/StationImage")
	@RequiresHeader({ "Authorization", "Content-Type" })
	ApiResponseUpLoad StationImage(MultiValueMap<String, Object> multiValueMap);

	// 局站列表
	@Get("/stationList?stationName={stationName}")
	ApiResponse stationList(String stationName) throws RestClientException;

	// 局站列表
	@Get("/stationList")
	ApiResponseImage stationListImage() throws RestClientException;

	@Post("/creationUser")
	ApiResponse creationUser(LinkedHashMap<String, String> items) throws RestClientException;

	@Get("/newGrouping?GroupingName={GroupingName}&substation_id={substation_id}")
	ApiResponse newGrouping(String GroupingName, String substation_id);

	// 分组
	@Get("/Group?substation_id={substation_id}")
	ApiResponse Group(String substation_id);

	// 删除数据
	@Get("/deleteStation?station_id={station_id}")
	ApiResponse deleteStation(String station_id);
	
	@Get("/get_spdev_list")
	SPDevResponse get_spdev_list();
	
	@Get("/get_door_status?data_id={data_id}")
	ApiResponse GetDoorStatus(String data_id);
	
	@Post("/open_door")
	ApiResponse OpenDoor(DoorOperation op);
	

}
