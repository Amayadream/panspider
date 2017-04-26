package com.amayadream.panspider.crawler;

import com.amayadream.panspider.common.util.HttpClientUtils;

/**
 * @author :  Amayadream
 * @date :  2017.04.24 23:17
 */
public class FollowListTest {

    public static void main(String[] args) {
        String follow_list_url = "http://yun.baidu.com/pcloud/friend/getfollowlist?query_uk=%s&limit=24&start=%s";
        String result = HttpClientUtils.getRequest(String.format(follow_list_url, 892398881, 1));
        System.out.println(result);
    }

}
