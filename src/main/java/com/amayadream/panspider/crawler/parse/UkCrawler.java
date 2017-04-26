package com.amayadream.panspider.crawler.parse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amayadream.panspider.common.util.Constants;
import com.amayadream.panspider.common.util.HttpClientUtils;
import com.amayadream.panspider.common.util.RedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;

/**
 * @author :  Amayadream
 * @date :  2017.04.26 23:04
 */
@Component
public class UkCrawler {

    private static Logger logger = LoggerFactory.getLogger(UkCrawler.class);

    @Resource
    private RedisManager redisManager;

    /**
     * 获取热门用户并补充到uk_list中
     */
    public void getHotUK(Jedis jedis) throws InterruptedException {
        Integer i = 0;
        String url;
        while (true) {
            url = Constants.URL_HOT_UK.replace("{start}", String.valueOf(i));
            i ++;
            String result = HttpClientUtils.getRequest(url);
            if (result == null) {   //被封..
                Thread.sleep(1000);
                continue;
            }
            JSONObject o = JSON.parseObject(result);
            if (o.getInteger("errno") == 0) {
                JSONArray arr = JSON.parseArray(o.getString("hotuser_list"));
                if (arr.size() != 0) {
                    logger.info("正在爬取第" + i + "页数据");
                    arr.forEach(o1 -> {
                        JSONObject u = JSON.parseObject(String.valueOf(o1));
                        jedis.rpush(Constants.REDIS_KEY_UK_LIST, u.getString("hot_uk"));
                    });
                } else
                    break;
            }
        }
        logger.info("爬取hot_uk结束");
    }

    /**
     * 根据uk获取到其所有粉丝的uk
     */
    public void getFans(Jedis jedis, String uk) throws InterruptedException {
        Integer i = 0;
        String url;
        while (true) {
            url = Constants.URL_FANS.replace("{start}", String.valueOf(i)).replace("{uk}", uk);
            i ++;
            String result = HttpClientUtils.getRequest(url);
            if (result == null) {   //被封..
                Thread.sleep(1000);
                continue;
            }
            JSONObject o = JSON.parseObject(result);
            if (o.getInteger("errno") == 0) {
                JSONArray arr = JSON.parseArray(o.getString("fans_list"));
                if (arr.size() != 0) {
                    logger.info("正在爬取第" + i + "页数据");
                    arr.forEach(o1 -> {
                        JSONObject u = JSON.parseObject(String.valueOf(o1));
                        jedis.rpush(Constants.REDIS_KEY_UK_LIST, u.getString("fans_uk"));
                    });
                } else
                    break;
            }
        }
        logger.info("爬取{} fans结束", uk);
    }

    /**
     * 根据uk获取到其所有
     * @param jedis
     */
    public void getFollow(Jedis jedis, String uk) throws InterruptedException {
        Integer i = 0;
        String url;
        while (true) {
            url = Constants.URL_FOLLOW.replace("{start}", String.valueOf(i)).replace("{uk}", uk);
            i ++;
            String result = HttpClientUtils.getRequest(url);
            if (result == null) {   //被封..
                Thread.sleep(1000);
                continue;
            }
            JSONObject o = JSON.parseObject(result);
            if (o.getInteger("errno") == 0) {
                JSONArray arr = JSON.parseArray(o.getString("follow_list"));
                if (arr.size() != 0) {
                    logger.info("正在爬取第" + i + "页数据");
                    arr.forEach(o1 -> {
                        JSONObject u = JSON.parseObject(String.valueOf(o1));
                        jedis.rpush(Constants.REDIS_KEY_UK_LIST, u.getString("follow_uk"));
                    });
                } else
                    break;
            }
        }
        logger.info("爬取{} fans结束", uk);
    }

}
