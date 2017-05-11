package com.amayadream.panspider.crawler.exec;

import com.alibaba.fastjson.JSONArray;
import com.amayadream.panspider.common.util.Constants;
import com.amayadream.panspider.common.util.Requests;
import com.amayadream.panspider.proxy.ProxyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 * @author : Amayadream
 * @date : 2017-04-27 09:24
 */
public class ShareCrawler implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(ShareCrawler.class);

    private Jedis jedis;
    private Storage storage;
    private ProxyManager proxyManager;

    public ShareCrawler(Jedis jedis, Storage storage, ProxyManager proxyManager) {
        this.jedis = jedis;
        this.storage = storage;
        this.proxyManager = proxyManager;
    }

    @Override
    public void run() {
        String uk;
        while ((uk = storage.consume(jedis)) != null) {
            getFollow(jedis, storage, uk);
        }
    }

    /**
     * 根据uk获取到其所有订阅
     */
    public void getFollow(Jedis jedis, Storage storage, String uk) {
        logger.info("[share]uk{} uk共享文件爬取任务开始", uk);

        Integer i = 0;
        String url;
        while (true) {
            url = Constants.URL_SHARE.replace("{start}", String.valueOf(i)).replace("{uk}", uk);
            JSONArray result = null;
            try {
                result = Requests.parseResult(Requests.getRequest(url, proxyManager, Constants.HTTP_HEADER_REFERER), "records");
                if (result == null) {
                    logger.warn("[share]uk{} 第{}页爬取异常, 暂时休眠后继续", uk, i);
                    continue;
                }
                if (result.size() != 0) {
                    logger.info("[share]uk{} 正在爬取第{}页数据", uk, i);
                    result.forEach(o1 -> storage.saveShare(jedis, String.valueOf(o1)));
                } else
                    break;
            } catch (Exception e) {
                //被封禁, 切换代理然后重试
                logger.warn("[share]uk{} 第{}页检测到被封禁ip, 正在尝试切换代理", uk, i);
                proxyManager.switchProxy();
                continue;
            }

            i ++;
        }

        logger.info("[share]uk{} uk共享文件爬取任务结束", uk);
    }

}
