package com.amayadream.panspider.crawler;

import com.amayadream.panspider.crawler.exec.ShareCrawlerTask;
import com.amayadream.panspider.crawler.exec.UkCrawlerTask;
import com.amayadream.panspider.crawler.exec.UkStorage;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author :  Amayadream
 * @date :  2017.04.24 23:15
 */
public class TaskExecutor {

    public static void main(String[] args) {
        JedisPool pool = new JedisPool();
        Jedis jedis = pool.getResource();

        UkStorage storage = new UkStorage();

        ExecutorService service = Executors.newCachedThreadPool();
        UkCrawlerTask product1 = new UkCrawlerTask(jedis, storage);
        UkCrawlerTask product2 = new UkCrawlerTask(jedis, storage);
        UkCrawlerTask product3 = new UkCrawlerTask(jedis, storage);
        ShareCrawlerTask consume1 = new ShareCrawlerTask(jedis, storage);
        ShareCrawlerTask consume2 = new ShareCrawlerTask(jedis, storage);

        service.execute(product1);
        service.execute(product2);
        service.execute(product3);

        service.execute(consume1);
        service.execute(consume2);

    }

}
