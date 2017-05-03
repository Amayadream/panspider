package com.amayadream.panspider.crawler.exec;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amayadream.panspider.common.util.Constants;
import com.amayadream.panspider.common.util.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 * 热门uk爬取线程
 * @author :  Amayadream
 * @date :  2017.05.01 14:42
 */
public class HotUkCrawlerTask implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(HotUkCrawlerTask.class);

    private Jedis jedis;
    private UkStorage storage;

    public HotUkCrawlerTask(Jedis jedis, UkStorage storage) {
        this.jedis = jedis;
        this.storage = storage;
    }

    /**
     * 从热门uk接口中获取热门的uk
     */
    @Override
    public void run() {
        try {
            getHotUk(jedis, storage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取热门用户并补充到uk_list中
     */
    public void getHotUk(Jedis jedis, UkStorage storage) throws InterruptedException {
        logger.info("[hot]热门uk爬取任务开始");

        int i = 0;
        String url;
        while (true) {
            url = Constants.URL_HOT_UK.replace("{start}", String.valueOf(i));
            String result = HttpClientUtils.getRequest(url);
            if (result == null) {
                logger.warn("[hot]第{}次爬取异常, 暂时休眠后继续", i);
                Thread.sleep(Constants.THREAD_SLEEP_HOT_ERROR);
                continue;
            }
            JSONObject o = JSON.parseObject(result);
            if (o.getInteger("errno") == 0) {
                JSONArray arr = JSON.parseArray(o.getString("hotuser_list"));
                if (arr.size() != 0) {
                    logger.info("[hot]正在爬取第{}页数据", i);
                    arr.forEach(o1 -> {
                        JSONObject u = JSON.parseObject(String.valueOf(o1));
                        storage.product(jedis, u.getString("hot_uk"));
                    });
                    //成功后休眠1s
                    Thread.sleep(Constants.THREAD_SLEEP_HOT_COMMON);
                } else
                    break;
            } else {
                logger.warn("[hot]第{}次爬取异常, 暂时休眠后继续", i);
                Thread.sleep(Constants.THREAD_SLEEP_HOT_ERROR);
                continue;
            }
            i ++;
        }

        logger.info("[hot]热门uk爬取任务结束");
    }

}
