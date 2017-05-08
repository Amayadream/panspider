package com.amayadream.panspider.crawler;

import com.amayadream.panspider.AbstractSpringTest;
import com.amayadream.panspider.common.util.Constants;
import com.amayadream.panspider.common.util.RedisManager;
import com.amayadream.panspider.crawler.exec.UkFollowCrawlerTask;
import com.amayadream.panspider.crawler.exec.UkStorage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author :  Amayadream
 * @date :  2017.04.26 21:39
 */
public class SimpleRedisTest extends AbstractSpringTest {

    @Test
    public void test1() {
        jedis.rpush(Constants.REDIS_KEY_UK_LIST, "Java");
        jedis.rpush(Constants.REDIS_KEY_UK_LIST, "Python");
        jedis.rpush(Constants.REDIS_KEY_UK_LIST, "Scala");
        jedis.rpush(Constants.REDIS_KEY_UK_LIST, "JavaScript");
        jedis.rpush(Constants.REDIS_KEY_UK_LIST, "Ruby");
        jedis.rpush(Constants.REDIS_KEY_UK_LIST, "NodeJS");
        jedis.rpush(Constants.REDIS_KEY_UK_LIST, "C++");

        jedis.blpop(1000, Constants.REDIS_KEY_UK_LIST);
        jedis.blpop(1000, Constants.REDIS_KEY_UK_LIST);
        jedis.blpop(1000, Constants.REDIS_KEY_UK_LIST);
        jedis.blpop(1000, Constants.REDIS_KEY_UK_LIST);
        jedis.blpop(1000, Constants.REDIS_KEY_UK_LIST);
        jedis.blpop(1000, Constants.REDIS_KEY_UK_LIST);
        jedis.blpop(1000, Constants.REDIS_KEY_UK_LIST);
        jedis.blpop(1000, Constants.REDIS_KEY_UK_LIST);
        jedis.blpop(1000, Constants.REDIS_KEY_UK_LIST);

    }


}
