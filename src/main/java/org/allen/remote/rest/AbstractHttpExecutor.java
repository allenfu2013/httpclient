package org.allen.remote.rest;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractHttpExecutor implements HttpOperation {

    protected abstract CloseableHttpClient getHttpClient();

    @Override
    public <T> T get(String url, Map<String, String> headers, Map<String, String> parameters, Class<T> t, int timeout) throws HttpException {
        if (parameters != null && parameters.size() > 0) {
            StringBuilder queryString = new StringBuilder("?");
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                try {
                    queryString.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8")).append("&");
                } catch (UnsupportedEncodingException e) {
                    throw new HttpException(e.getMessage(), e);
                }
            }
            url += queryString.substring(0, queryString.length() - 1);
        }
        HttpGet httpGet = new HttpGet(url);
        return doExecute(httpGet, headers, t, timeout);
    }

    @Override
    public <T> T post(String url, Map<String, String> headers, Map<String, String> parameters, Class<T> t, int timeout) throws HttpException {
        HttpPost httpPost = new HttpPost(url);
        if (parameters != null && parameters.size() > 0) {
            List<NameValuePair> params = new ArrayList<>();
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new HttpException(e.getMessage(), e);
            }
        }
        return doExecute(httpPost, headers, t, timeout);
    }

    @Override
    public <T> T postJson(String url, Map<String, String> headers, String json, Class<T> t, int timeout) throws HttpException {
        StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(stringEntity);
        return doExecute(httpPost, headers, t, timeout);
    }

    private <T> T doExecute(HttpRequestBase request, Map<String, String> headers, Class<T> t, int timeout) throws HttpException {
        // set request header
        request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36");
        request.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        request.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
        request.setHeader("Accept-Charset", "ISO-8859-1,utf-8,gbk,gb2312;q=0.7,*;q=0.7");

        // set request read timeout
        if (timeout > 0) {
            request.setConfig(RequestConfig.custom().setSocketTimeout(timeout).build());
        } else {
            System.out.println("http client timeout is set to no-timeout!!!");
        }

        // set request header
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                request.setHeader(header.getKey(), header.getValue());
            }
        }

        CloseableHttpResponse response = null;
        InputStream in = null;
        try {
            long t1 = System.currentTimeMillis();
            response = getHttpClient().execute(request);
            long t2 = System.currentTimeMillis();
            System.out.println(String.format("##### request: %s, took: %s", request.getURI(), (t2 - t1)));
            in = response.getEntity().getContent();
            String content = IOUtils.toString(in, "UTF-8");
            if (t == String.class) {
                return (T) content;
            } else {
                long t3 = System.currentTimeMillis();
                T ret = JSON.parseObject(content, t);
                // T ret = new Gson().fromJson(content, t);
                long t4 = System.currentTimeMillis();
                System.out.println("##### parseJson took: " + (t4 - t3));
                return ret;
            }
        } catch (Exception e) {
            throw new HttpException(e.getMessage(), e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw new HttpException(e.getMessage(), e);
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    throw new HttpException(e.getMessage(), e);
                }
            }
        }
    }
}
