package org.allen.remote.rest;

import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.util.Map;

public class HttpRemoteService extends AbstractHttpExecutor {

    private CloseableHttpClient httpClient;
    private HttpClientConfig httpClientConfig;

    public HttpRemoteService() {
        System.out.println("************** init http client *******************");
        this.httpClientConfig = new HttpClientConfig();
        httpClient = HttpClientGenerator.create().setHttpClientConfig(httpClientConfig).generate();
    }

    public HttpRemoteService(HttpClientConfig httpClientConfig) {
        System.out.println("************** init http client ******************");
        httpClient = HttpClientGenerator.create().setHttpClientConfig(httpClientConfig).generate();
    }

    public void setHttpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public <T> T get(String url, Class<T> t) throws HttpException {
        return get(url, null, null, t, httpClientConfig.getSocketTimeout());
    }

    public <T> T get(String url, Class<T> t, int timeout) throws HttpException {
        return get(url, null, null, t, timeout);
    }

    public <T> T get(String url, Map<String, String> parameters, Class<T> t) throws HttpException {
        return get(url, null, parameters, t, httpClientConfig.getSocketTimeout());
    }

    public <T> T get(String url, Map<String, String> parameters, Class<T> t, int timeout) throws HttpException {
        return get(url, null, parameters, t, timeout);
    }

    public <T> T post(String url, Class<T> t) throws HttpException {
        return post(url, null, null, t, httpClientConfig.getSocketTimeout());
    }

    public <T> T post(String url, Class<T> t, int timeout) throws HttpException {
        return post(url, null, null, t, timeout);
    }

    public <T> T post(String url, Map<String, String> parameters, Class<T> t) throws HttpException {
        return post(url, null, parameters, t, httpClientConfig.getSocketTimeout());
    }

    public <T> T post(String url, Map<String, String> parameters, Class<T> t, int timeout) throws HttpException {
        return post(url, null, parameters, t, timeout);
    }

    public <T> T postJson(String url, String json, Class<T> t) throws HttpException {
        return postJson(url, null, json, t, httpClientConfig.getSocketTimeout());
    }

    public <T> T postJson(String url, String json, Class<T> t, int timeout) throws HttpException {
        return postJson(url, null, json, t, timeout);
    }

    @Override
    protected CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    public void close() {
        System.out.println("************** close http client ******************");
        try {
            httpClient.close();
        } catch (IOException e) {
            throw new HttpException(e.getMessage(), e);
        }
    }

}
