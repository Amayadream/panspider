package com.amayadream.panspider.crawler.exec;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amayadream.panspider.common.util.Constants;
import com.amayadream.panspider.common.util.Requests;
import com.amayadream.panspider.crawler.proxy.ProxyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 * uk订阅爬取线程
 * @author :  Amayadream
 * @date :  2017.05.01 14:43
 */
public class FollowCrawler implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(FollowCrawler.class);

    private Jedis jedis;
    private UkStorage storage;
    private ProxyManager proxyManager;

    public FollowCrawler(Jedis jedis, UkStorage storage, ProxyManager proxyManager) {
        this.jedis = jedis;
        this.storage = storage;
        this.proxyManager = proxyManager;
    }

    @Override
    public void run() {
        try {
            String uk;
            while ((uk = storage.get(jedis, Constants.REDIS_KEY_UK_EXIST_FOLLOW_LIST)) != null) {
                getFollow(jedis, storage, uk);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据uk获取到其所有订阅
     */
    public void getFollow(Jedis jedis, UkStorage storage, String uk) throws InterruptedException {
        logger.info("[follow]uk{} 订阅uk爬取任务开始", uk);

        Integer i = 0;
        String url;
        while (true) {
            url = Constants.URL_FOLLOW.replace("{start}", String.valueOf(i)).replace("{uk}", uk);
            JSONArray result = null;
            try {
                result = Requests.parseResult(Requests.getRequest(url, proxyManager), "follow_list");
                if (result == null) {
                    logger.warn("[follow]uk{} 第{}页爬取异常, 暂时休眠后继续", uk, i);
                    continue;
                }
                if (result.size() != 0) {
                    logger.info("[follow]uk{} 正在爬取第{}页数据", uk, i);
                    result.forEach(o1 -> {
                        JSONObject u = JSON.parseObject(String.valueOf(o1));
                        storage.product(jedis, u.getString("follow_uk"));
                    });
                } else
                    break;
            } catch (Exception e) {
                //被封禁, 切换代理然后重试
                logger.warn("[follow]uk{} 第{}页检测到被封禁ip, 正在尝试切换代理", uk, i);
                proxyManager.switchProxy();
                continue;
            }

            i ++;
        }

        logger.info("[follow]uk{} 订阅uk爬取任务结束", uk);
    }

}
