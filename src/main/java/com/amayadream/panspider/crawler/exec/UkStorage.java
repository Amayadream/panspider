package com.amayadream.panspider.crawler.exec;

import com.amayadream.panspider.common.util.Constants;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * storage
 * @author: xjding
 * @date: 2017-04-27 09:25
 */
public class UkStorage {

    /**
     * 生产单条uk
     */
    public synchronized void product(Jedis jedis, String uk) {
        //只有未存在才存入待处理队列中
        if (jedis.sadd(Constants.REDIS_KEY_UK_EXIST_SET, uk) > 0){
            jedis.rpush(Constants.REDIS_KEY_UK_LIST, uk);
        }
    }

    /**
     * 生产多条uk
     */
    public synchronized void product(Jedis jedis, String...uks) {
        if (uks.length > 0) {
            for (String uk : uks) {
                product(jedis, uk);
            }
        }
    }

    /**
     * 从临时队列中取出一条uk, 查询粉丝和订阅
     * 如果临时队列为空, 则复制uk_exist_set的元素填充到临时队列中
     */
    public synchronized String get(Jedis jedis, String key) {
        if (jedis.llen(key) == 0) {
            //如果队列为空, 则复制uk_exist_set元素填充到队列中
            Set<String> set = jedis.smembers(Constants.REDIS_KEY_UK_EXIST_SET);
            String[] arr = new String[]{};
            arr = set.toArray(arr);
            if (arr.length == 0)
                return get(jedis, key);
            jedis.rpush(key, arr);
            return get(jedis, key);
        } else {
            //如果不为空则取出最新的一条
            return jedis.blpop(0, key).get(1);
        }
    }

    /**
     * 消费单条uk
     */
    public synchronized String consume(Jedis jedis) {
        //从队列中取出一条uk
        String uk = jedis.blpop(0, Constants.REDIS_KEY_UK_LIST).get(1);
        //判断是否已经处理过
        if (!jedis.sismember(Constants.REDIS_KEY_UK_USED_SET, uk)) {
            //如果未处理则加入已处理列表
            jedis.sadd(Constants.REDIS_KEY_UK_USED_SET, uk);
            return uk;
        } else {
            //如果已经处理过则重复这次操作
            return consume(jedis);
        }
    }

}
