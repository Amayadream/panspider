package com.amayadream.panspider.search;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import static org.elasticsearch.common.xcontent.XContentFactory.*;
import static org.elasticsearch.index.query.QueryBuilders.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author :  Amayadream
 * @date :  2017.05.13 22:29
 */
public class ElasticSearchTest {

    private TransportClient client;

    @Before
    public void before() throws UnknownHostException {
        client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
    }

    @Test
    public void clientTest() throws IOException {
        XContentBuilder share = jsonBuilder()
                .startObject()
                .field("shareId", "1")
                .field("shortId", "1x")
                .field("title", "编程语言")
                .array("shareFile", "Java", "Python", "Scala", "C++")
                .endObject();
        IndexResponse response = client.prepareIndex("share", "share", "2")
                .setSource(share)
                .get();
        RestStatus status = response.status();
        System.out.println(status.getStatus());

    }

    @Test
    public void get() {
        GetResponse response = client.prepareGet("share", "share", "1").get();
        System.out.println(JSON.toJSONString(response.getSource()));
    }

    @Test
    public void search() {
        QueryBuilder qb = multiMatchQuery(
                "语言",
                "title", "shareFile"
        );

        SearchResponse response = client.prepareSearch("share")
                .addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC)
                .setScroll(new TimeValue(60000))
                .setQuery(qb)
                .setSize(100).get();
        do {
            for (SearchHit hit : response.getHits().getHits()) {
                System.out.println(JSON.toJSONString(hit.getSource()));
            }
            response = client.prepareSearchScroll(response.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
        } while (response.getHits().getHits().length != 0);
    }

    @After
    public void after() {
        client.close();
    }

}
