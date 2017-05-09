package com.amayadream.panspider.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author: xjding
 * @date: 2017-05-09 16:36
 */
public class Requests {

    private static Logger logger = LoggerFactory.getLogger(Requests.class);

    private static final int timeout = 10 * 1000;
    private static final int retry = 5;

    private static CloseableHttpClient httpClient = null;

    private final static Object syncLock = new Object();

    private static Header[] HTTP_COMMON_HEADER = {
            new BasicHeader(HttpHeaders.ACCEPT, "*/*"),
            new BasicHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36"),
            new BasicHeader(HttpHeaders.CONNECTION, "keep-alive"),
            new BasicHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate"),
            new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.8"),
    };

    /**
     * 获取httpclient
     * @param url 请求地址
     * @return 创建的httpclient
     */
    public static CloseableHttpClient getHttpClient(String url) {
        String hostname = url.split("/")[2];
        int port = 80;
        if (hostname.contains(":")) {
            String[] arr = hostname.split(":");
            hostname = arr[0];
            port = Integer.valueOf(arr[1]);
        }
        if (httpClient == null) {
            synchronized (syncLock) {
                if (httpClient == null)
                    httpClient = createHttpClient(200, 40, 100, hostname, port);
            }
        }
        return httpClient;
    }

    /**
     * 创建httpclient
     * @param maxTotal      最大连接数
     * @param maxPerRoute   路由基础链接
     * @param maxRoute      路由最大链接
     * @param hostname      主机地址
     * @param port          主机端口号
     * @return 创建的httpclient
     */
    public static CloseableHttpClient createHttpClient(int maxTotal, int maxPerRoute, int maxRoute, String hostname,
                                                       int port) {
        ConnectionSocketFactory plainFactory = PlainConnectionSocketFactory.getSocketFactory();
        LayeredConnectionSocketFactory sslFactory = SSLConnectionSocketFactory.getSocketFactory();
        final Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", plainFactory)
                .register("https", sslFactory)
                .build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        // 将最大连接数增加
        cm.setMaxTotal(maxTotal);
        // 将每个路由基础的连接增加
        cm.setDefaultMaxPerRoute(maxPerRoute);
        HttpHost httpHost = new HttpHost(hostname, port);
        // 将目标主机的最大连接数增加
        cm.setMaxPerRoute(new HttpRoute(httpHost), maxRoute);

        //请求重试机制
        HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException e, int i, HttpContext httpContext) {
                if (i >= retry)
                    return false;
                if (e instanceof NoHttpResponseException)   // 如果服务器丢掉了连接, 重试
                    return true;
                if (e instanceof SSLHandshakeException)     //SSL握手异常, 放弃
                    return false;
                if (e instanceof InterruptedIOException)    //超时
                    return false;
                if (e instanceof UnknownHostException)      // 目标服务器不可达
                    return false;
                if (e instanceof SSLException) // SSL异常
                    return false;
                HttpClientContext ctx = HttpClientContext.adapt(httpContext);
                HttpRequest request = ctx.getRequest();
                //如果请求是幂等的, 就再次尝试
                return !(request instanceof HttpEntityEnclosingRequest);
            }
        };

        return HttpClients.custom()
                .setConnectionManager(cm)
                .setRetryHandler(retryHandler).build();
    }

    /**
     * 解析返回值
     * @param result    返回值
     * @param listKey   list的key
     * @return  返回list
     */
    public static JSONArray parseResult(String result, String listKey) throws Exception {
        if (result == null) return null;
        if (result.contains("\"errno\":-55"))
            throw new Exception("ip is forbidden!");
        JSONObject o = JSON.parseObject(result);
        String list = o.getString(listKey);
        if (list != null) {
            return JSONArray.parseArray(list);
        }
        return null;
    }

    /**
     * get请求
     * @param url       请求地址
     * @return String类型的返回值
     */
    public static String getRequest(String url) {
        return getRequest(url, null);
    }

    /**
     * get请求
     * @param url       请求地址
     * @param header    http请求头
     * @return String类型的返回值
     */
    public static String getRequest(String url, Header header) {
        HttpGet httpGet = new HttpGet(url);
        setRequestConfig(httpGet);
        if (header != null)
            httpGet.setHeaders(ArrayUtils.add(HTTP_COMMON_HEADER, header));
        return execute(httpGet, url);
    }

    /**
     * post请求
     * @param url       请求地址
     * @param params    map形式的请求参数
     * @return String类型的返回值
     */
    public static String postRequest(String url, Map<String, Object> params) {
        return postRequest(url, params, null);
    }

    /**
     * post请求
     * @param url       请求地址
     * @param params    map形式的请求参数
     * @param header    http头
     * @return String类型的返回值
     */
    public static String postRequest(String url, Map<String, Object> params, Header header) {
        HttpPost httpPost = new HttpPost(url);
        setRequestConfig(httpPost);
        setPostParams(httpPost, params);
        if (header != null)
            httpPost.setHeaders(ArrayUtils.add(HTTP_COMMON_HEADER, header));
        return execute(httpPost, url);
    }

    /**
     * 执行请求并解析返回结果
     * @param httpRequestBase   请求
     * @param url               请求地址
     * @return String类型的返回值
     */
    private static String execute(HttpRequestBase httpRequestBase, String url) {
        CloseableHttpResponse response = null;
        try {
            response = getHttpClient(url).execute(httpRequestBase, HttpClientContext.create());
            if (response.getStatusLine().getStatusCode() == 200) {
                logger.info("[requests]请求成功， 状态码: 200!");
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity, "utf-8");
                EntityUtils.consume(entity);
                return result;
            }
            logger.warn("[requests]请求错误, 状态码: " + response.getStatusLine().getStatusCode());
        } catch (IOException e) {
            logger.error("[requests]执行请求中出错, 错误原因: " + e.getMessage());
        } finally {
            try {
                if (response != null)
                    response.close();
            } catch (IOException e) {
                logger.error("[requests]关闭response失败!");
            }
        }
        return null;
    }

    /**
     * 设置请求参数
     * @param httpRequestBase 请求
     */
    private static void setRequestConfig(HttpRequestBase httpRequestBase) {
        setRequestConfig(httpRequestBase, null);
    }

    /**
     * 设置请求参数
     * @param httpRequestBase 请求
     * @param proxy           代理
     */
    private static void setRequestConfig(HttpRequestBase httpRequestBase, HttpHost proxy) {
        RequestConfig.Builder builder = RequestConfig.custom()
                .setConnectionRequestTimeout(timeout)
                .setConnectTimeout(timeout)
                .setSocketTimeout(timeout);
        if (proxy != null)
            builder.setProxy(proxy);
        httpRequestBase.setConfig(builder.build());
    }

    /**
     * 为post请求设置参数
     * @param httpPost  post请求
     * @param params    map形式的参数
     */
    private static void setPostParams(HttpPost httpPost, Map<String, Object> params) {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            pairs.add(new BasicNameValuePair(key, params.get(key).toString()));
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(pairs, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error("[requests]不支持的编码格式!");
        }
    }

}