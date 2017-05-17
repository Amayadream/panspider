package com.amayadream.panspider.crawler;

import com.amayadream.panspider.common.util.ElasticSearchManager;
import com.amayadream.panspider.crawler.exec.*;
import com.amayadream.panspider.proxy.ProxyCrawler;
import com.amayadream.panspider.proxy.ProxyManager;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.context.WebApplicationContext;
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
        ElasticSearchManager elasticSearchManager = factory.getBean(ElasticSearchManager.class);

        Jedis jedis1 = pool.getResource();
        Jedis jedis2 = pool.getResource();
        Jedis jedis3 = pool.getResource();
        Jedis jedis4 = pool.getResource();
        Jedis jedis5 = pool.getResource();
        Jedis jedis6 = pool.getResource();
        Jedis jedis7 = pool.getResource();

        Storage storage = new Storage();
        ProxyManager proxyManager = ProxyManager.getInstance(jedis1);

        ExecutorService service = Executors.newCachedThreadPool();
        ExecutorService validatorService = Executors.newFixedThreadPool(3);

        ProxyCrawler proxyCrawler = new ProxyCrawler(jedis2, validatorService);

        UkCrawler hotUkTask = new UkCrawler(jedis3, storage, proxyManager);
        FansCrawler fansTask = new FansCrawler(jedis4, storage, proxyManager);
        FollowCrawler followTask = new FollowCrawler(jedis5, storage, proxyManager);

        ShareCrawler shareCrawler = new ShareCrawler(jedis6, storage, proxyManager);

        ShareSave shareSave = new ShareSave(jedis7, storage, mongoTemplate, elasticSearchManager);

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
