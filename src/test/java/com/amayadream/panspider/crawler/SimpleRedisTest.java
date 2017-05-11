package com.amayadream.panspider.crawler;

import com.amayadream.panspider.AbstractSpringTest;
import com.amayadream.panspider.common.util.Constants;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;

/**
 * @author :  Amayadream
 * @date :  2017.04.26 21:39
 */
public class SimpleRedisTest extends AbstractSpringTest {

    @Test
    public void test1() {
        jedis.rpush(Constants.REDIS_KEY_UK_LIST, "Java");
        jedis.rpush(Constants.REDIS_KEY_UK_LIST, "Python");
        jedis.rpush(Constants.REDIS_KEY_UK_LIST, "Scala");
        jedis.rpush(Constants.REDIS_KEY_UK_LIST, "JavaScript");
        jedis.rpush(Constants.REDIS_KEY_UK_LIST, "Ruby");
        jedis.rpush(Constants.REDIS_KEY_UK_LIST, "NodeJS");
        jedis.rpush(Constants.REDIS_KEY_UK_LIST, "C++");

        jedis.blpop(1000, Constants.REDIS_KEY_UK_LIST);
        jedis.blpop(1000, Constants.REDIS_KEY_UK_LIST);
        jedis.blpop(1000, Constants.REDIS_KEY_UK_LIST);
        jedis.blpop(1000, Constants.REDIS_KEY_UK_LIST);
        jedis.blpop(1000, Constants.REDIS_KEY_UK_LIST);
        jedis.blpop(1000, Constants.REDIS_KEY_UK_LIST);
        jedis.blpop(1000, Constants.REDIS_KEY_UK_LIST);
        jedis.blpop(1000, Constants.REDIS_KEY_UK_LIST);
        jedis.blpop(1000, Constants.REDIS_KEY_UK_LIST);

    }

    @Test
    public void test() {
        CloseableHttpClient client = HttpClients.createDefault();
        RequestConfig config = RequestConfig.custom()
                .setConnectionRequestTimeout(5000)
                .setConnectTimeout(5000)
                .setSocketTimeout(5000)
                .setProxy(new HttpHost("112.80.248.76", 80))
                .build();
        HttpGet get = new HttpGet("http://pan.baidu.com/pcloud/friend/getfanslist?query_uk=137575187&limit=24&start=60");
        get.setConfig(config);
        try {
            CloseableHttpResponse response = client.execute(get);
            System.out.println(response.getStatusLine().getStatusCode());
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
