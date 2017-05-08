package com.amayadream.panspider.crawler.proxy;

import com.amayadream.panspider.common.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 * 代理管理器, 单例实现
 * @author :  Amayadream
 * @date :  2017.05.07 22:26
 */
public class ProxyManager {

    private static Logger logger = LoggerFactory.getLogger(ProxyManager.class);

    public static ProxyManager proxyManager;

    private Jedis jedis;
    private String[] ipAndPort;

    private ProxyManager(Jedis jedis) {
        this.jedis = jedis;
    }

    public synchronized static ProxyManager getInstance(Jedis jedis) {
        if (proxyManager == null)
            proxyManager = new ProxyManager(jedis);
        return proxyManager;
    }

    /**
     * 切换代理
     */
    public synchronized void switchProxy() {
        String proxy = jedis.srandmember(Constants.REDIS_KEY_PROXY_IP);
        if (proxy == null) {
            logger.warn("[switchProxy]代理已经消耗完毕, 等待30秒后继续执行!");
            try {
                Thread.sleep(30000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            switchProxy();
        } else {
            ipAndPort = proxy.split(":");
            logger.info("[switchProxy]切换成功, 当前代理: {}", proxy);
        }
    }

    /**
     * 获取当前的代理
     */
    public synchronized String[] getProxy() {
        return ipAndPort;
    }

    /**
     * 删除不可用代理
     */
    public void removeProxy(String[] ipAndPort) {
        jedis.srem(Constants.REDIS_KEY_PROXY_IP, ipAndPort[0] + ":" + ipAndPort[1]);
    }
}
