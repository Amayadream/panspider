package com.amayadream.panspider.crawler;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author: xjding
 * @date: 2017-04-26 09:17
 */
public class RedisManager {

    private JedisPool jedisPool;

    public RedisManager(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public synchronized Jedis initResource() {
        if (jedisPool == null)
            return null;
        return jedisPool.getResource();
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }
}
