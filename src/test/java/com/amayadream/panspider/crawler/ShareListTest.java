package com.amayadream.panspider.crawler;

import com.amayadream.panspider.common.util.Constants;
import com.amayadream.panspider.common.util.Requests;

/**
 * @author: Amayadream
 * @date: 2017-04-25 09:30
 */
public class ShareListTest {

    public static void main(String[] args) {
        String follow_list_url = "http://pan.baidu.com/pcloud/feed/getsharelist?auth_type=1&request_location=share_home&start=0&limit=60&query_uk=224534490";
        String result = Requests.getRequest(follow_list_url, null, Constants.HTTP_HEADER_REFERER);
        System.out.println(result);
    }

}
