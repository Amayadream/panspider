package com.amayadream.panspider.crawler;

import com.amayadream.panspider.AbstractSpringTest;
import com.amayadream.panspider.common.util.Constants;
import org.junit.Test;

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
