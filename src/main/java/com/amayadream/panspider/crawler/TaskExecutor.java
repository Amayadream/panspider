package com.amayadream.panspider.crawler;

import com.amayadream.panspider.crawler.exec.*;
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

        UkStorage storage = new UkStorage();

        ExecutorService service = Executors.newCachedThreadPool();

        HotUkCrawlerTask hotUkTask = new HotUkCrawlerTask(jedis1, storage);
        UkFansCrawlerTask fansTask = new UkFansCrawlerTask(jedis2, storage);
        UkFollowCrawlerTask followTask = new UkFollowCrawlerTask(jedis3, storage);

        service.execute(hotUkTask);
        Thread.sleep(10000);
        service.execute(fansTask);
        service.execute(followTask);
    }

}
