package com.amayadream.panspider.proxy;

import com.amayadream.panspider.AbstractSpringTest;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: xjding
 * @date: 2017-05-05 17:37
 */
public class ProxyTest extends AbstractSpringTest {

    @Test
    public void getProxyIp() {
        ExecutorService validateService = Executors.newFixedThreadPool(5);
        ProxyCrawler crawler = new ProxyCrawler(jedis, validateService);
        crawler.run();
    }

}
