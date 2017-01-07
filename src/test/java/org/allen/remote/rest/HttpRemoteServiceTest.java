package org.allen.remote.rest;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class HttpRemoteServiceTest {

    private static HttpRemoteService httpRemoteService = new HttpRemoteService();

    long start;
    long end;

    @Before
    public void before() {
        start = System.currentTimeMillis();
    }

    @After
    public void after() {
        end = System.currentTimeMillis();
        System.out.println("#### " + (end - start));

    }

    @AfterClass
    public static void afterClass() {
        httpRemoteService.close();
    }

    @Test
    public void testGet() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("format", "json");
        parameters.put("ip", "180.168.36.45");
        String result = httpRemoteService.get("http://int.dpool.sina.com.cn/iplookup/iplookup.php", parameters, String.class);
        System.out.println(result);
    }

    //    @Test
    public void testGet2() {
        String url = "http://localhost:8080/springmvc/user/get-by-name";
        Map<String, String> param = new HashMap<>();
        param.put("name", "张%三");
        String result = httpRemoteService.get(url, param, String.class);
        System.out.println(result);
    }

    @Test
    public void getGet3() {
        String result = httpRemoteService.get("https://localhost:8443/imocker/admin/query-all-api", String.class);
        System.out.println(result);
    }

}
