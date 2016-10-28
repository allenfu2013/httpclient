package org.allen.remote.rest;

import com.google.gson.Gson;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
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
    public void testDoHttpGet() throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("format", "json");
        parameters.put("ip", "180.168.36.45");
        String result = httpClientSample.doHttpGet("http://int.dpool.sina.com.cn/iplookup/iplookup.php", parameters, String.class);
        System.out.println(result);
    }

    @Test
    public void testDoHttpGet2() throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("format", "json");
        parameters.put("ip", "180.168.36.45");
        IpLookupResponse result = httpClientSample.doHttpGet("http://int.dpool.sina.com.cn/iplookup/iplookup.php", parameters, IpLookupResponse.class);
        System.out.println(new Gson().toJson(result));
        System.out.println("country: " + result.getCountry());
        System.out.println("province: " + result.getProvince());
        System.out.println("city: " + result.getCity());
    }

}
