package org.allen.remote.rest;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * simple http client sample
 */
public class HttpClientSample {

    private CloseableHttpClient httpClient;

    public HttpClientSample() {
        // simple http client
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }
                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[]{tm}, null);
            httpClient = HttpClientBuilder.create()
                    .setSSLContext(sslContext)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * get request
     * @param url
     * @param parameters
     * @param t
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> T get(String url, Map<String, String> parameters, Class<T> t) throws Exception {
        if (parameters != null && parameters.size() > 0) {
            StringBuilder queryString = new StringBuilder("?");
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                queryString.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8")).append("&");
            }
            url += queryString.substring(0, queryString.length() - 1);
        }

        HttpGet httpGet = new HttpGet(url);
        return doExecute(httpGet, t);
    }

    /**
     * post request
     * support urlencoded
     * @param url
     * @param parameters
     * @param t
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> T post(String url, Map<String, String> parameters, Class<T> t) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        if (parameters != null && parameters.size() > 0) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        }

        return doExecute(httpPost, t);
    }

    /**
     * post request
     * support json body
     * @param url
     * @param json
     * @param t
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> T postJson(String url, String json, Class<T> t) throws Exception {
        StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(stringEntity);
        return doExecute(httpPost, t);
    }

    private <T> T doExecute(HttpUriRequest request, Class<T> t) throws IOException {
        CloseableHttpResponse response = null;
        InputStream in = null;
        try {
            long t1 = System.currentTimeMillis();
            response = httpClient.execute(request);
            long t2 = System.currentTimeMillis();
            System.out.println(String.format("##### request: %s, took: %s", request.getURI(), (t2-t1)));
            in = response.getEntity().getContent();
            String content = IOUtils.toString(in, "UTF-8");
            if (t == String.class) {
                return (T) content;
            } else {
                long t3 = System.currentTimeMillis();
                T ret = JSON.parseObject(content, t);
                // T ret = new Gson().fromJson(content, t);
                long t4 = System.currentTimeMillis();
                System.out.println("#################### parseJson took: " + (t4 - t3));
                return ret;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw e;
                }
            }
            if (response != null){
                try {
                    response.close();
                } catch (IOException e) {
                    throw e;
                }
            }
        }
    }
}
