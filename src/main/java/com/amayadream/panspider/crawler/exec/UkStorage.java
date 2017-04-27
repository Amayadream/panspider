package com.amayadream.panspider.crawler.exec;

import com.alibaba.fastjson.JSON;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 *
 * @author: xjding
 * @date: 2017-04-27 09:25
 */
public class UkStorage {

    public static final String UK_KEY = "uk_list";

    /**
     * 生产
     */
    public void product(Jedis jedis, String uk) {
        jedis.rpush(UK_KEY, uk);
    }

    /**
     * 生产多条
     */
    public void product(Jedis jedis, String...uks) {
        if (uks.length > 0) {
            jedis.rpush(UK_KEY, uks);
        }
    }

    /**
     * 消费uk
     */
    public String consume(Jedis jedis) {
        return jedis.blpop(0, UK_KEY).get(1);
    }

    public static void main(String[] args) {
        JedisPool pool = new JedisPool();
        Jedis jedis = pool.getResource();
        UkStorage storage = new UkStorage();
        storage.product(jedis, "Java", "JavaScript", "Python", "Scala", "C++");

        System.out.println(JSON.toJSONString(storage.consume(jedis)));
    }


}
