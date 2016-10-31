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
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

public class HttpClientGenerator {

    private HttpClientConfig clientConfig;

    public HttpClientGenerator setHttpClientConfig(HttpClientConfig clientConfig) {
        this.clientConfig = clientConfig;
        return this;
    }

    public static HttpClientGenerator create() {
        return new HttpClientGenerator();
    }

    public CloseableHttpClient generate() {
        if (clientConfig == null) {
            clientConfig = new HttpClientConfig();
        }
        return HttpClientBuilder.create().setConnectionManager(buildClientConnManager()).build();
    }

    private HttpClientConnectionManager buildClientConnManager() {
        LayeredConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
                buildSSLContext(),
                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        PoolingHttpClientConnectionManager clientConnManager = new PoolingHttpClientConnectionManager(
                RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("http", PlainConnectionSocketFactory.getSocketFactory())
                        .register("https", sslSocketFactory)
                        .build(),
                null, null, null, clientConfig.getConnTimeToLive(), TimeUnit.MILLISECONDS);

        clientConnManager.setMaxTotal(clientConfig.getMaxTotal());
        clientConnManager.setDefaultMaxPerRoute(clientConfig.getMaxPerRoute());

        SocketConfig socketConfig = SocketConfig.custom()
                .setSoKeepAlive(clientConfig.isSocketKeepAlive())
                .setSoTimeout(clientConfig.getSocketTimeout())
                .setTcpNoDelay(clientConfig.isTcpNoDelay())
                .setSoLinger(clientConfig.getSocketLinger())
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
}
