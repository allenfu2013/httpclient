package org.allen.remote.rest;

import java.util.List;
import java.util.Map;

public interface HttpOperation {

    <T> T get(String url, Map<String, String> headers, Map<String, String> parameters, Class<T> t, int timeout) throws HttpException;

    <T> List<T> getList(String url, Map<String, String> headers, Map<String, String> parameters, Class<T> t, int timeout) throws HttpException;

    <T> T post(String url, Map<String, String> headers, Map<String, String> parameters, Class<T> t, int timeout) throws HttpException;

    <T> List<T> postList(String url, Map<String, String> headers, Map<String, String> parameters, Class<T> t, int timeout) throws HttpException;

    <T> T postJson(String url, Map<String, String> headers, String json, Class<T> t, int timeout) throws HttpException;

    <T> List<T> postJsonList(String url, Map<String, String> headers, String json, Class<T> t, int timeout) throws HttpException;
}