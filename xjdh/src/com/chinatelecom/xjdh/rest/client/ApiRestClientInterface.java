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
import com.chinatelecom.xjdh.rest.interceptor.HttpBasicAuthenticatorInterceptor;
import com.chinatelecom.xjdh.utils.URLs;
import com.chinatelecom.xjdh.utils.Update;

@Rest(rootUrl = URLs.URL_API_HOST + "/api", converters = { MappingJacksonHttpMessageConverter.class, StringHttpMessageConverter.class,
		FormHttpMessageConverter.class, ResourceHttpMessageConverter.class, ByteArrayHttpMessageConverter.class }, interceptors = { HttpBasicAuthenticatorInterceptor.class })
@RequiresHeader("Authorization")
public interface ApiRestClientInterface extends RestClientHeaders {
	@Get("/getuserinfo")
	ApiResponse getUserInfo() throws RestClientException;

	@Get("/getAlarmList?citycode={citycode}&countycode={countycode}&substationId={substationId}&roomId={roomId}&level={level}&model={model}&startdatetime={startdatetime}&enddatetime={enddatetime}&offset={offset}&count={count}&lastId={lastId}")
	ApiResponse getAlarmList(String citycode, String countycode, String substationId, String roomId, String level, String model, String startdatetime,
			String enddatetime, String offset, String count, String lastId) throws RestClientException;

	@Get("/getAreaData")
	ApiResponse getAreaData() throws RestClientException;

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
}
