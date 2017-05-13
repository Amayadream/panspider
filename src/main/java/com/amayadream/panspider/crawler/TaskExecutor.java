package com.amayadream.panspider.crawler;

import com.amayadream.panspider.crawler.exec.*;
import com.amayadream.panspider.proxy.ProxyCrawler;
import com.amayadream.panspider.proxy.ProxyManager;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
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
        MongoTemplate mongoTemplate = factory.getBean(MongoTemplate.class);

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

        ShareSave shareSave = new ShareSave(jedis, storage, mongoTemplate);

        //1.爬取代理
        service.execute(proxyCrawler);
        Thread.sleep(50000);

        //2.爬取热门uk
        service.execute(hotUkTask);
        Thread.sleep(10000);

        //3.开启粉丝/订阅爬取
        service.execute(fansTask);
        service.execute(followTask);

        //4.开启share爬取
        service.execute(shareCrawler);

        //5.开启share构建索引和入库
        service.execute(shareSave);

    }

}
