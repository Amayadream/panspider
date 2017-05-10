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

    public static String URL_HOT_UK = "http://pan.baidu.com/pcloud/friend/gethotuserlist?start={start}&limit=24";
    public static String URL_FANS = "http://pan.baidu.com/pcloud/friend/getfanslist?query_uk={uk}&limit=24&start={start}";
    public static String URL_FOLLOW = "http://pan.baidu.com/pcloud/friend/getfollowlist?query_uk={uk}&limit=24&start={start}";
    public static String URL_SHARE = "http://pan.baidu.com/pcloud/feed/getsharelist?auth_type=1&request_location=share_home&start={start}&limit=60&query_uk={uk}";

    public static Header HTTP_HEADER_REFERER = new BasicHeader(HttpHeaders.REFERER, "https://pan.baidu.com/share/home");

    /** 存储待处理uk的redis list */
    public static final String REDIS_KEY_UK_LIST = "uk_list";
    /** 存储使用过的uk的set */
    public static final String REDIS_KEY_UK_USED_SET = "uk_used_set";
    /** 存储已存在的uk的set */
    public static final String REDIS_KEY_UK_EXIST_SET = "uk_exist_set";
    /** 存储等待爬取粉丝和订阅的uk list */
    public static final String REDIS_KEY_UK_EXIST_FOLLOW_LIST = "uk_exist_follow_list";
    public static final String REDIS_KEY_UK_EXIST_FANS_LIST = "uk_exist_fans_list";
    /** 存储代理库的set */
    public static final String REDIS_KEY_PROXY_IP_SET = "proxy_ip_set";
    public static final String REDIS_KEY_SHARE_LIST = "share_list";


    /** 代理获取接口 */
    public static final String PROXY_URL_XICI = "http://api.xicidaili.com/free2016.txt";
    public static final String PROXY_URL_66IP = "http://www.66ip.cn/nmtq.php?getnum=100&isp=0&anonymoustype=0&start=&ports=&export=&ipaddress=&area=0&proxytype=2&api=66ip";




}
