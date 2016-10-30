package org.allen.remote.rest;

import java.util.Map;

public interface HttpOperation {

    <T> T get(String url, Map<String, String> parameters, Class<T> t, int timeout) throws HttpException;

    <T> T post(String url, Map<String, String> parameters, Class<T> t, int timeout) throws HttpException;

    <T> T postJson(String url, String json, Class<T> t, int timeout) throws HttpException;
}