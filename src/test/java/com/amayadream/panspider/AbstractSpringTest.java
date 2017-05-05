package com.amayadream.panspider;

import com.amayadream.panspider.common.util.RedisManager;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;

/**
 * @author :  Amayadream
 * @date :  2017.04.24 22:53
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-test.xml")
public abstract class AbstractSpringTest {

    @Resource
    private RedisManager redisManager;

    protected Jedis jedis;

    @Before
    public void before() {
        jedis = redisManager.initResource();
    }

    @After
    public void after() {
        redisManager.releaseResource(jedis);
    }

}
