package com.amayadream.panspider.crawler.exec;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amayadream.panspider.common.util.Constants;
import com.amayadream.panspider.common.util.ElasticSearchManager;
import com.amayadream.panspider.crawler.model.Share;
import com.amayadream.panspider.crawler.model.ShareFile;
import org.elasticsearch.rest.RestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Amayadream
 * @date :   2017-05-10 15:20
 */
public class ShareSave implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(ShareSave.class);

    private Jedis jedis;
    private Storage storage;
    private MongoTemplate mongoTemplate;
    private ElasticSearchManager elasticSearchManager;

    public ShareSave(Jedis jedis, Storage storage, MongoTemplate mongoTemplate, ElasticSearchManager elasticSearchManager) {
        this.jedis = jedis;
        this.storage = storage;
        this.mongoTemplate = mongoTemplate;
        this.elasticSearchManager = elasticSearchManager;
    }

    @Override
    public void run() {
        String value;
        while ((value = storage.getShare(jedis)) != null) {

            //1.入库
            JSONObject record = JSON.parseObject(value);
            if (StringUtils.isEmpty(record))    continue;
            Share share = new Share(record.getString("shareid"), record.getString("shorturl"),
                    record.getString("title"), record.getString("desc"),
                    record.getLongValue("feed_time"), record.getIntValue("category"),
                    record.getIntValue("filecount"), record.getIntValue("dir_cnt"),
                    record.getString("uk"), record.getString("username"),
                    record.getString("avatar_url"));
            JSONArray filelist = record.getJSONArray("filelist");
            if (filelist != null && filelist.size() > 0) {
                List<ShareFile> shareFiles = new ArrayList<ShareFile>();
                filelist.forEach(
                        (item) -> {
                            if (!StringUtils.isEmpty(item)) {
                                JSONObject o = JSON.parseObject(String.valueOf(item));
                                ShareFile shareFile = new ShareFile(o.getString("server_filename"),
                                        o.getIntValue("category"), o.getLongValue("size"),
                                        o.getString("md5"), o.getString("sign"),
                                        o.getIntValue("isdir"), o.getLongValue("time_stamp"));
                                shareFiles.add(shareFile);
                            }
                        }
                );
                share.setFiles(shareFiles);
            }
            mongoTemplate.save(share);
            logger.info("[shareSave] {} 已存入mongo库中!", share.getShareId());

            //2.构建索引
            buildIndex(share);
        }
    }

    /**
     * 构建索引
     */
    private void buildIndex(Share share) {
        int time = 0;
        while (time < Constants.ES_BUILD_INDEX_TIME) {
            time ++;
            try {
                RestStatus status = elasticSearchManager.index(share);
                if (status.getStatus() == 200 || status.getStatus() == 201 || status.getStatus() == 202)
                    break;
                logger.warn("[ShareSave] 构建索引失败, 失败代码: {}", status.getStatus());
            } catch (IOException e) {
                logger.warn("[ShareSave] 构建索引错误, 错误原因: {}", e.getMessage());
            }
        }
    }

}
