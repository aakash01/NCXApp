package com.anutanetworks.ncxapp.services;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.Map;

/**
 * Created by Aakash on 7/3/2015.
 */
public class AnutaRestClient {

    private static String BASE_URL ;

    private static AsyncHttpClient client = null;


    public static void initializeClient(Map<String, String> paramMap){
        BASE_URL = paramMap.get("host_url");
        client = new AsyncHttpClient();
        String actualUsername = paramMap.get("username");
        String password = paramMap.get("password");
        String organization = paramMap.get("organization");

        if(ValidationUtils.isNotNull(organization)){
            actualUsername += "|" + organization;
        }
        client.addHeader("AnutaAPIVersion","2.0");
        client.addHeader("Accept","application/json");
        client.addHeader("Content-type","application/json");
        client.setBasicAuth(actualUsername, password);
    }

    public static void get(String url, RequestParams requestParams, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), requestParams, responseHandler);
    }

    public static void post(String url,RequestParams requestParams, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), requestParams, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
