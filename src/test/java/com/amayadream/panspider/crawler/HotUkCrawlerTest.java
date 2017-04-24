package com.amayadream.panspider.crawler;

import com.amayadream.panspider.BaseSpringTest;
import com.amayadream.panspider.crawler.parse.HotUkCrawler;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @author :  Amayadream
 * @date :  2017.04.24 22:58
 */
public class HotUkCrawlerTest extends BaseSpringTest {

    @Resource
    private HotUkCrawler crawler;

    @Test
    public void test() throws InterruptedException {
        crawler.getHotUK();
    }

}
