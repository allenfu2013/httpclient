package org.allen.remote.rest;

import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

public class ConfigHttpClientSample extends AbstractHttpExecutor {

    private CloseableHttpClient httpClient;

    private PoolingHttpClientConnectionManager clientConnManager;

    private int maxTotal = 200;
    private int maxPerRoute = 20;
    private int socketTimeout = 3000;
    private boolean socketKeepAlive = true;
    private boolean tcpNoDelay = true;
    private int socketLinger = 1000;
    private int connTimeToLive = 5000;

    public ConfigHttpClientSample() {
        httpClient = HttpClientBuilder.create()
                .setConnectionManager(buildClientConnManager())
                .build();
    }

    private HttpClientConnectionManager buildClientConnManager() {
        LayeredConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(buildSSLContext());

        clientConnManager = new PoolingHttpClientConnectionManager(
                RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("http", PlainConnectionSocketFactory.getSocketFactory())
                        .register("https", sslSocketFactory)
                        .build(),
                null, null, null, connTimeToLive, TimeUnit.MILLISECONDS);

        clientConnManager.setMaxTotal(maxTotal);
        clientConnManager.setDefaultMaxPerRoute(maxPerRoute);

        SocketConfig socketConfig = SocketConfig.custom()
                .setSoKeepAlive(socketKeepAlive)
                .setSoTimeout(socketTimeout)
                .setTcpNoDelay(tcpNoDelay)
                .setSoLinger(socketLinger)
                .build();

        clientConnManager.setDefaultSocketConfig(socketConfig);

        return clientConnManager;
    }

    private SSLContext buildSSLContext() throws HttpException {
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
        } catch (Exception e) {
            throw new HttpException(e.getMessage(), e);
        }
        return sslContext;
    }

    @Override
    protected CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    public void close() {
        try {
            clientConnManager.close();
            httpClient.close();
        } catch (IOException e) {
            throw new HttpException(e.getMessage(), e);
        }
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMaxPerRoute() {
        return maxPerRoute;
    }

    public void setMaxPerRoute(int maxPerRoute) {
        this.maxPerRoute = maxPerRoute;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public boolean isSocketKeepAlive() {
        return socketKeepAlive;
    }

    public void setSocketKeepAlive(boolean socketKeepAlive) {
        this.socketKeepAlive = socketKeepAlive;
    }

    public boolean isTcpNoDelay() {
        return tcpNoDelay;
    }

    public void setTcpNoDelay(boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
    }

    public int getSocketLinger() {
        return socketLinger;
    }

    public void setSocketLinger(int socketLinger) {
        this.socketLinger = socketLinger;
    }
}
