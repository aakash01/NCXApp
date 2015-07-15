package com.anutanetworks.ncxapp.services;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.apache.http.HttpEntity;

import java.security.KeyStore;
import java.util.Map;

/**
 * Created by Aakash on 7/3/2015.
 */
public class AnutaRestClient {

    private static String BASE_URL;

    private static AsyncHttpClient client = null;

    private static boolean allowOffline = false;

    public static boolean isAllowOffline(){
        return allowOffline;
    }


    public static void initializeClient(Map<String, String> paramMap) {
        BASE_URL = paramMap.get("host_url");
        client = new AsyncHttpClient();
        String actualUsername = paramMap.get("username");
        String password = paramMap.get("password");
        String organization = paramMap.get("organization");
        allowOffline = Boolean.valueOf(paramMap.get("allowOffline"));

        if (ValidationUtils.isNotNull(organization)) {
            actualUsername += "|" + organization;
        }
        client.addHeader("AnutaAPIVersion", "2.0");
        client.addHeader("Accept", "application/json");
        client.addHeader("Content-type", "application/json");
        client.setBasicAuth(actualUsername, password);
        if(BASE_URL.startsWith("https")){
            try {
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);
                MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
                sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                client.setSSLSocketFactory(sf);
            }
            catch (Exception e) {
            }
        }
    }

    public static void get(String url, RequestParams requestParams, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), requestParams, responseHandler);
    }

    public static void post(String url, RequestParams requestParams, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), requestParams, responseHandler);
    }

    public static void post(Context context, String url, HttpEntity entity, ResponseHandlerInterface responseHandler) {
        client.post(context, getAbsoluteUrl(url), entity, "application/json", responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
