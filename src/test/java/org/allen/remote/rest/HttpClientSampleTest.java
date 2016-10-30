package org.allen.remote.rest;

import com.google.gson.Gson;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class HttpClientSampleTest {

    private HttpClientSample httpClientSample = new HttpClientSample();

    long start;
    long end;

    @Before
    public void before() {
        start = System.currentTimeMillis();
    }

    @After
    public void after() {
        end = System.currentTimeMillis();
        System.out.println("####### " + (end - start));
    }

    @Test
    public void testDoHttpGet() throws Exception {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("format", "json");
        parameters.put("ip", "180.168.36.45");
        String result = httpClientSample.get("http://int.dpool.sina.com.cn/iplookup/iplookup.php", parameters, String.class);
        System.out.println(result);
    }

    @Test
    public void testDoHttpGet2() throws Exception {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("format", "json");
        parameters.put("ip", "180.168.36.45");
        IpLookupResponse result = httpClientSample.get("http://int.dpool.sina.com.cn/iplookup/iplookup.php", parameters, IpLookupResponse.class);
        System.out.println(new Gson().toJson(result));
        System.out.println("country: " + result.getCountry());
        System.out.println("province: " + result.getProvince());
        System.out.println("city: " + result.getCity());
    }

    @Test
    public void testDoHttpGet3() throws Exception {
        String url = "http://localhost:8080/springmvc/user/get-by-name";
        Map<String, String> param = new HashMap<>();
        param.put("name", "张%三");
        String result = httpClientSample.get(url, param, String.class);
        System.out.println(result);
    }

    @Test
    public void testDoHttpPost() throws Exception {
        Map<String, String> param = new HashMap<>();
        param.put("id", "15");
        param.put("name", "张%三");
        String result = httpClientSample.post("http://localhost:8080/springmvc/user/add", param, String.class);
        System.out.println(result);
    }

    @Test
    public void testDoHttpPostJson() throws Exception {
        String json = "{\"name\":\"孙大%圣\"}";
        String result = httpClientSample.postJson("http://localhost:8080/springmvc/user/create", json, String.class);
        System.out.println(result);
    }

    @Test
    public void testDoHttpsGet() throws Exception {
        String result = httpClientSample.get("https://localhost:8443/imocker/admin/query-all-api", null, String.class);
        System.out.println(result);
    }
}
