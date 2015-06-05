package com.chinateltecom.xjdh.rest.client;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.RequiresHeader;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.RestClientHeaders;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestClientException;

import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.utils.URLs;
import com.chinateltecom.xjdh.rest.interceptor.HttpBasicAuthenticatorInterceptor;

@Rest(rootUrl = URLs.URL_API_HOST + "/api", converters = { MappingJacksonHttpMessageConverter.class, StringHttpMessageConverter.class,
		FormHttpMessageConverter.class, ResourceHttpMessageConverter.class, ByteArrayHttpMessageConverter.class }, interceptors = { HttpBasicAuthenticatorInterceptor.class })
@RequiresHeader("Authorization")
public interface ApiRestClientInterface extends RestClientHeaders {
	@Get("/getuserinfo")
	ApiResponse getUserInfo() throws RestClientException;
}
