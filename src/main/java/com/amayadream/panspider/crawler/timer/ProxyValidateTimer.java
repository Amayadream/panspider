package com.amayadream.panspider.crawler.timer;

import com.amayadream.panspider.common.util.Constants;
import com.amayadream.panspider.common.util.RedisManager;
import com.amayadream.panspider.proxy.ProxyValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.Set;

/**
 * redis代理池定时检测, 剔除失效的代理
 * @author :  Amayadream
 * @date :  2017.05.07 22:14
 */
@Component
public class ProxyValidateTimer {

    private static Logger logger = LoggerFactory.getLogger(ProxyValidateTimer.class);

    @Resource
    private RedisManager redisManager;

    /**
     * 每五分钟定时清理失效的代理
     */
    @Scheduled(cron = "* 5/5 * * * ?")
    public void validate() {
        logger.info("[proxyValidateTimer]开始进行定时代理清理!");
        Jedis jedis = redisManager.initResource();
        try {
            Set<String> set = jedis.smembers(Constants.REDIS_KEY_PROXY_IP_SET);
            if (set == null || set.size() == 0) return;
            set.forEach(
                item -> {
                    String[] ipAndPort = item.split(":");
                    if (!ProxyValidator.validateProxyIp(ipAndPort[0], Integer.valueOf(ipAndPort[1]))) {
                        jedis.srem(Constants.REDIS_KEY_PROXY_IP_SET, item);
                    }
                }
            );
        } finally {
            redisManager.releaseResource(jedis);
        }
        logger.info("[proxyValidateTimer]结束定时代理清理!");
    }

}
