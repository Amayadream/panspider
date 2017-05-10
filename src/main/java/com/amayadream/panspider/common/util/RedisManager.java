package com.amayadream.panspider.common.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author : Amayadream
 * @date : 2017-04-26 09:17
 */
public class RedisManager {

    private JedisPool jedisPool;

    public RedisManager(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    /**
     * 获取Jedis对象
     * @return
     */
    public synchronized Jedis initResource() {
        if (jedisPool == null)
            return null;
        return jedisPool.getResource();
    }

    /**
     * 释放Jedis对象
     * @param jedis
     */
    public synchronized void releaseResource(Jedis jedis) {
        if (jedis != null)
            jedis.close();
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }
}
