package org.allen.remote.rest;

public class HttpException extends RuntimeException {

    public HttpException(String msg, Throwable e) {
        super(msg, e);
    }
}
