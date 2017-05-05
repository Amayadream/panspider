package com.amayadream.panspider.common.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * httpclient工具类
 * @author :  Amayadream
 * @date :  2017.04.19 21:39
 */
public class HttpClientUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    public static Header[] HTTP_COMMON_HEADER = {
            new BasicHeader(HttpHeaders.ACCEPT, "*/*"),
            new BasicHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36"),
            new BasicHeader(HttpHeaders.CONNECTION, "keep-alive"),
            new BasicHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate"),
            new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.8"),
    };

    private static final RequestConfig HTTP_REQUEST_CONFIG = RequestConfig.custom()
            .setConnectTimeout(5000)
            .setSocketTimeout(5000)
            .setConnectionRequestTimeout(5000)
            .build();

    public static String getRequest(String url){
        return getRequest(url, null, null);
    }

    public static String getRequest(String url, Header[] headers) {
        return getRequest(url, headers, null);
    }

    public static String getRequest(String url, String[] ipAndPort) {
        return getRequest(url, null, ipAndPort);
    }

    public static String getRequest(String url, Header[] headers, String[] ipAndPort) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        if (!StringUtils.isEmpty(ipAndPort)) {
            HttpHost proxy = new HttpHost(ipAndPort[0], Integer.valueOf(ipAndPort[1]));
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(5000)
                    .setSocketTimeout(5000)
                    .setConnectionRequestTimeout(5000)
                    .setProxy(proxy)
                    .build();
            get.setConfig(config);
        }else {
            get.setConfig(HTTP_REQUEST_CONFIG);
        }
        get.setHeaders(HTTP_COMMON_HEADER);
        if (headers != null && headers.length > 0) {
            get.setHeaders(ArrayUtils.addAll(HTTP_COMMON_HEADER, headers));
        }
        HttpResponse response = null;
        HttpEntity entity = null;
        try {
            response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == 200) {
                entity = response.getEntity();
                return EntityUtils.toString(entity, Constants.CHARSET_UTF8);
            }
            logger.warn("请求发生错误, 状态码: {}", response.getStatusLine().getStatusCode());
            return null;
        } catch (IOException e) {
            logger.error("请求失败, 错误原因: {}", e.getMessage());
            return null;
        } finally {
            try {
                EntityUtils.consume(entity);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
