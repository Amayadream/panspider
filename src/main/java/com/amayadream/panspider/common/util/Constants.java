package com.amayadream.panspider.common.util;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.message.BasicHeader;

/**
 * 常量定义
 * @author :  Amayadream
 * @date :  2017.04.21 23:30
 */
public class Constants {

    public static final String CHARSET_UTF8 = "utf-8";

    public static String URL_HOT_UK = "http://yun.baidu.com/pcloud/friend/gethotuserlist?start={start}&limit=24";
    public static String URL_FANS = "http://pan.baidu.com/pcloud/friend/getfanslist?query_uk={uk}&limit=24&start={start}";
    public static String URL_FOLLOW = "http://yun.baidu.com/pcloud/friend/getfollowlist?query_uk={uk}&limit=24&start={start}";

    public static Header HTTP_HEADER_REFERER = new BasicHeader(HttpHeaders.REFERER, "https://pan.baidu.com/share/home");

    /** 存储待处理uk的redis list */
    public static final String REDIS_KEY_UK_LIST = "uk_list";
    /** 存储使用过的uk的set */
    public static final String REDIS_KEY_UK_USED_SET = "uk_used_set";
    /** 存储已存在的uk的set */
    public static final String REDIS_KEY_UK_EXIST_SET = "uk_exist_set";
    /** 存储等待爬取粉丝和订阅的uk list */
    public static final String REDIS_KEY_UK_EXIST_TEMP_LIST = "uk_exist_temp_list";


}
