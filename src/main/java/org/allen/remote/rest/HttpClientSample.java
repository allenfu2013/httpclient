package org.allen.remote.rest;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.sun.istack.internal.NotNull;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * simple http client sample
 */
public class HttpClientSample {

    private HttpClient httpClient;

    public HttpClientSample() {
        // simple http client
        httpClient = HttpClientBuilder.create().build();
    }

    public <T> T doHttpGet(@NotNull String url, Map<String, String> parameters, Class<T> t) throws IOException {
        if (parameters != null && parameters.size() > 0) {
            StringBuilder queryString = new StringBuilder("?");
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                queryString.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8")).append("&");
            }
            url += queryString.substring(0, queryString.length() - 1);
        }

        HttpGet httpGet = new HttpGet(url);
        long t1 = System.currentTimeMillis();
        HttpResponse httpResponse = httpClient.execute(httpGet);
        long t2 = System.currentTimeMillis();
        System.out.println(String.format("############### request: %s, took: %s", url, (t2 - t1)));
        String content = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
        if (t == String.class) {
            return (T) content;
        } else {
            long t3 = System.currentTimeMillis();
            T ret = JSON.parseObject(content, t);
//            T ret = new Gson().fromJson(content, t);
            long t4 = System.currentTimeMillis();
            System.out.println("#################### parseJson took: " + (t4 - t3));
            return ret;
        }
    }

    public <T> T doHttpPost(@NotNull String url, Map<String, String> parameters, Class<T> t) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        if (parameters != null && parameters.size() > 0) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        }

        long t1 = System.currentTimeMillis();
        HttpResponse httpResponse = httpClient.execute(httpPost);
        long t2 = System.currentTimeMillis();
        System.out.println(String.format("##### request: %s, took: %s", url, (t2-t1)));
        String content = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
        if (t == String.class) {
            return (T) content;
        } else {
            long t3 = System.currentTimeMillis();
            T ret = JSON.parseObject(content, t);
//            T ret = new Gson().fromJson(content, t);
            long t4 = System.currentTimeMillis();
            System.out.println("#################### parseJson took: " + (t4 - t3));
            return ret;
        }
    }

    public <T> T doHttpPostJson(String url, String json, Class<T> t) throws Exception {
        StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(stringEntity);

        long t1 = System.currentTimeMillis();
        HttpResponse httpResponse = httpClient.execute(httpPost);
        long t2 = System.currentTimeMillis();
        System.out.println(String.format("##### request: %s, took: %s", url, (t2-t1)));
        String content = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
        if (t == String.class) {
            return (T) content;
        } else {
            long t3 = System.currentTimeMillis();
            T ret = JSON.parseObject(content, t);
//            T ret = new Gson().fromJson(content, t);
            long t4 = System.currentTimeMillis();
            System.out.println("#################### parseJson took: " + (t4 - t3));
            return ret;
        }
    }
}
