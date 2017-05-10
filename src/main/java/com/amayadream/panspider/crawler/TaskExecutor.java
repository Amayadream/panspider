package com.amayadream.panspider.crawler;

import com.amayadream.panspider.crawler.exec.UkCrawler;
import com.amayadream.panspider.crawler.exec.FansCrawler;
import com.amayadream.panspider.crawler.exec.FollowCrawler;
import com.amayadream.panspider.crawler.exec.UkStorage;
import com.amayadream.panspider.crawler.proxy.ProxyManager;
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
        Jedis jedis1 = pool.getResource();
        Jedis jedis2 = pool.getResource();
        Jedis jedis3 = pool.getResource();
        Jedis jedis4 = pool.getResource();

        UkStorage storage = new UkStorage();
        ProxyManager proxyManager = ProxyManager.getInstance(jedis4);

        ExecutorService service = Executors.newCachedThreadPool();

        UkCrawler hotUkTask = new UkCrawler(jedis1, storage, proxyManager);
        FansCrawler fansTask = new FansCrawler(jedis2, storage, proxyManager);
        FollowCrawler followTask = new FollowCrawler(jedis3, storage, proxyManager);

        service.execute(hotUkTask);
        Thread.sleep(10000);
        service.execute(fansTask);
        service.execute(followTask);
    }

}
