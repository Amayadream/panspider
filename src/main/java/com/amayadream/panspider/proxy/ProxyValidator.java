package com.amayadream.panspider.proxy;

import com.amayadream.panspider.common.util.Constants;
import com.amayadream.panspider.common.util.Requests;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.io.IOException;

/**
 * 代理ip验证类， 负责验证代理ip是否可用
 * @author :  Amayadream
 * @date :  2017.05.04 23:02
 */
public class ProxyValidator implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(ProxyCrawler.class);

    private Jedis jedis;
    private String host;
    private Integer port;

    ProxyValidator(Jedis jedis, String host, Integer port) {
        this.jedis = jedis;
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        if (validateProxyIp(host, port)) {
            jedis.sadd(Constants.REDIS_KEY_PROXY_IP_SET, host + ":" + port);
        }
    }

    /**
     * 验证代理ip是否可用
     */
    public static boolean validateProxyIp(String host, int port) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpHost proxy = new HttpHost(host, port);
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setSocketTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .setProxy(proxy)
                .build();
        HttpGet get = new HttpGet("http://pan.baidu.com/");
        get.setConfig(config);
        get.setHeaders(Requests.HTTP_COMMON_HEADER);
        HttpResponse response;
        try {
            response = client.execute(get);
            logger.info("代理地址{}:{}验证成功, 已录入代理库!", host, port);
            return response.getStatusLine().getStatusCode() == 200;
        } catch (IOException e) {
            logger.error("代理地址{}:{}验证失败, 已经丢弃!", host, port);
            return false;
        }
    }

}
