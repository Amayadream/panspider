package com.amayadream.panspider.common.util;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.message.BasicHeader;

/**
 * @author :  Amayadream
 * @date :  2017.04.21 23:30
 */
public class Constants {

    public static final String CHARSET_UTF8 = "utf-8";

    public static String HOT_UK_URL = "http://yun.baidu.com/pcloud/friend/gethotuserlist?type=1&from=feed&start={start}&limit=24&bdstoken=ac95ef31d3979f6ee707ef75cee9f5c5&clienttype=0&web=1";

    public static Header HTTP_HEADER_REFERER = new BasicHeader(HttpHeaders.REFERER, "https://pan.baidu.com/share/home");

}
