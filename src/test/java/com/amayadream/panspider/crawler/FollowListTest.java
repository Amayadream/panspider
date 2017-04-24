package com.amayadream.panspider.crawler;

import com.amayadream.panspider.common.util.HttpClientUtils;

/**
 * @author :  Amayadream
 * @date :  2017.04.24 23:17
 */
public class FollowListTest {

    public static void main(String[] args) {
        String follow_list_url = "http://yun.baidu.com/pcloud/friend/getfollowlist?query_uk=%s&limit=24&start=%s&bdstoken=e6f1efec456b92778e70c55ba5d81c3d&channel=chunlei&clienttype=0&web=1&logid=MTQ3NDA3NDg5NzU4NDAuMzQxNDQyMDY2MjA5NDA4NjU=";
        String result = HttpClientUtils.getRequest(String.format(follow_list_url, 892398881, 1));
    }

}
