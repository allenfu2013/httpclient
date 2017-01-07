# httpclient-demo

本项目对httpclient进行了封装，方便在spring配置使用：

```java

<bean id="httpClientConfig" class="org.allen.remote.rest.HttpClientConfig">
    <property name="maxTotal" value="100"/>
    <property name="maxPerRoute" value="10"/>
    <property name="socketTimeout" value="3000"/>
    <property name="socketKeepAlive" value="true"/>
    <property name="tcpNoDelay" value="true"/>
    <property name="socketLinger" value="1000"/>
    <property name="connTimeToLive" value="-1"/>
</bean>
    
<bean id="httpClient" class="org.allen.remote.rest.HttpRemoteService" destroy-method="close">
    <constructor-arg name="httpClientConfig" ref="httpClientConfig"/>
</bean>

```


GET

```java

Map<String, String> parameters = new HashMap<>();
parameters.put("format", "json");
parameters.put("ip", "180.168.36.45");
String result = httpClient.get("http://int.dpool.sina.com.cn/iplookup/iplookup.php", parameters, String.class);

```

POST

```java
String url = "http://localhost:8080/services/users/add";
Map<String, String> parameters = new HashMap<>();
// set parameters
User user = httpClient.post(url, parameter, User.class);

```

POST JSON

```java

String url = "http://localhost:8080/services/users/create";
String json = "{\"name\":\"张三\", \"age\":20}"
// set socket timeout
User user = httpClient.post(url, json, User.class, 3000);
```


