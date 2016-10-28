package org.allen.remote.rest;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.sun.istack.internal.NotNull;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

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
                queryString.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
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
//            T ret = JSON.parseObject(content, t);
            T ret = new Gson().fromJson(content, t);
            long t4 = System.currentTimeMillis();
            System.out.println("#################### parseJson took: " + (t4 - t3));
            return ret;
        }
    }
}
