package com.amayadream.panspider.crawler;

import com.amayadream.panspider.common.util.Requests;

/**
 * @author: Amayadream
 * @date: 2017-04-25 09:33
 */
public class FansListTest {

    public static void main(String[] args) {
        String url = "http://pan.baidu.com/pcloud/friend/getfanslist?query_uk=%s&limit=24&start=%s";
        String result = Requests.getRequest(String.format(url, 892398881, 1));
        System.out.println(result);
    }

}
