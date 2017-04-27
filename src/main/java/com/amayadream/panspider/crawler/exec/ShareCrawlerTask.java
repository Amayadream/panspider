package com.amayadream.panspider.crawler.exec;

import redis.clients.jedis.Jedis;

/**
 * @author: xjding
 * @date: 2017-04-27 09:24
 */
public class ShareCrawlerTask implements Runnable {

    private volatile boolean flag = false;

    private Jedis jedis;
    private UkStorage storage;

    public ShareCrawlerTask(Jedis jedis, UkStorage storage) {
        this.jedis = jedis;
        this.storage = storage;
    }

    @Override
    public void run() {
        while (!flag) {
            //TODO do somethingd
            storage.consume(jedis);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Jedis getJedis() {
        return jedis;
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }

    public UkStorage getStorage() {
        return storage;
    }

    public void setStorage(UkStorage storage) {
        this.storage = storage;
    }
}
