package com.chinateltecom.xjdh.rest.client;

import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.RestClientHeaders;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestClientException;

import com.chinatelecom.xjdh.bean.LoginResponse;
import com.chinatelecom.xjdh.bean.OauthParam;
import com.chinatelecom.xjdh.utils.URLs;
import com.chinateltecom.xjdh.rest.interceptor.HttpBasicAuthenticatorInterceptor;

@Rest(rootUrl = URLs.URL_API_HOST + "/oauth2", converters = { MappingJacksonHttpMessageConverter.class }, interceptors = { HttpBasicAuthenticatorInterceptor.class })
public interface OauthRestClientInterface extends RestClientHeaders {
	@Post("/authenticate")
	LoginResponse login(OauthParam param) throws RestClientException;
}
