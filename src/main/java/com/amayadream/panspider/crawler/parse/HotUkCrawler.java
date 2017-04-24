package com.amayadream.panspider.crawler.parse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amayadream.panspider.common.util.Constants;
import com.amayadream.panspider.common.util.HttpClientUtils;
import com.amayadream.panspider.crawler.model.HotUk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author :  Amayadream
 * @date :  2017.04.21 23:46
 */
@Component
public class HotUkCrawler {

    private static Logger logger = LoggerFactory.getLogger(HotUkCrawler.class);

    @Resource
    private MongoTemplate template;

    /**
     * 获取热门用户并补充到hot_uk_set中
     */
    public void getHotUK() throws InterruptedException {
        Integer i = 0;
        String url;
        Date now = new Date();
        HotUk uk = new HotUk(now, null);
        while (true) {
            url = Constants.HOT_UK_URL.replace("{start}", String.valueOf(i));
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
                        uk.setUk(u.getString("hot_uk"));
                        template.save(uk);
                    });
                } else
                    break;
            }
        }
        logger.info("爬取hot_uk结束");
    }

}
