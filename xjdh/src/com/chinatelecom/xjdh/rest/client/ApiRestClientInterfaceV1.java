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
@Rest(rootUrl = URLs.URL_API_HOST + "/api_" + URLs.API_VERSION, converters = { MappingJacksonHttpMessageConverter.class,
		StringHttpMessageConverter.class, MyFormHttpMessageConverter.class, MyStringHttpMessageConverter.class,
		ResourceHttpMessageConverter.class, FormHttpMessageConverter.class,
		ByteArrayHttpMessageConverter.class }, interceptors = { HttpBasicAuthenticatorInterceptor.class })
@RequiresHeader("Authorization")
public interface ApiRestClientInterfaceV1 extends RestClientHeaders {	
	@Get("/get_room_dev_list/{roomId}/{model}")
	ApiResponse get_room_dev_list(String roomId, String model);
	

}
