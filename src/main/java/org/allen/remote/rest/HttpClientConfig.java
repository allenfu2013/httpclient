package org.allen.remote.rest;

/**
 * http client configuration
 */
public class HttpClientConfig {

    // max connection = available + leased
    private int maxTotal = 200;
    // max connection per route
    private int maxPerRoute = 20;
    // socket read timeout
    private int socketTimeout = 3000;
    // socket keep alive
    private boolean socketKeepAlive = true;
    //
    private boolean tcpNoDelay = true;
    //
    private int socketLinger = 1000;
    //
    private int connTimeToLive = -1;

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

    public int getConnTimeToLive() {
        return connTimeToLive;
    }

    public void setConnTimeToLive(int connTimeToLive) {
        this.connTimeToLive = connTimeToLive;
    }
}
