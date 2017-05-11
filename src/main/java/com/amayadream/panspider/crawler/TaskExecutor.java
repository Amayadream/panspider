package com.amayadream.panspider.crawler;

import com.amayadream.panspider.crawler.exec.*;
import com.amayadream.panspider.proxy.ProxyCrawler;
import com.amayadream.panspider.proxy.ProxyManager;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author :  Amayadream
 * @date :  2017.04.24 23:15
 */
public class TaskExecutor {

    public static void main(String[] args) throws InterruptedException {

        ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"classpath*:spring/*.xml"});
        BeanFactory factory = (BeanFactory) context;

        JedisPool pool = factory.getBean(JedisPool.class);
        Jedis jedis = pool.getResource();

        Storage storage = new Storage();
        ProxyManager proxyManager = ProxyManager.getInstance(jedis);

        ExecutorService service = Executors.newCachedThreadPool();
        ExecutorService validatorService = Executors.newFixedThreadPool(3);

        ProxyCrawler proxyCrawler = new ProxyCrawler(jedis, validatorService);

        UkCrawler hotUkTask = new UkCrawler(jedis, storage, proxyManager);
        FansCrawler fansTask = new FansCrawler(jedis, storage, proxyManager);
        FollowCrawler followTask = new FollowCrawler(jedis, storage, proxyManager);

        ShareCrawler shareCrawler = new ShareCrawler(jedis, storage, proxyManager);

//        service.execute(proxyCrawler);
        Thread.sleep(5000);

        service.execute(hotUkTask);
        Thread.sleep(10000);

        service.execute(fansTask);
        service.execute(followTask);
        service.execute(shareCrawler);

    }

}
