package com.amayadream.panspider.common.util;

import com.amayadream.panspider.crawler.model.Share;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import static org.elasticsearch.common.xcontent.XContentFactory.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author :  Amayadream
 * @date :  2017.05.14 12:26
 */
public class ElasticSearchManager {

    private static Logger logger = LoggerFactory.getLogger(ElasticSearchManager.class);

    private static TransportClient client;

    private ElasticSearchManager(String host, int port) {
        try {
            client = new PreBuiltTransportClient(Settings.EMPTY)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
        } catch (UnknownHostException e) {
            logger.error("[ElasticSearchManager] 创建client失败, 错误原因: 未知的host {}", host);
            e.printStackTrace();
        }
    }

    /**
     * 创建索引
     * @param share 共享实体
     * @return
     * @throws IOException
     */
    public RestStatus index(Share share) throws IOException {
        XContentBuilder builder = null;
        try {
            if (share == null)  return null;
            List<String> fileNames = new ArrayList<String>();
            share.getFiles().forEach(
                    (f) -> fileNames.add(f.getFileName())
            );
            builder = jsonBuilder()
                    .startObject()
                    .field("shareId", share.getShareId())
                    .field("shortId", share.getShortId())
                    .field("title", share.getTitle())
                    .array("files", fileNames.toArray())
                    .endObject();
            IndexResponse response = client.prepareIndex("share", "share", share.getShareId())
                    .setSource(builder)
                    .get();
            return response.status();
        } finally {
            if (builder != null)
                builder.close();
        }

    }

}
