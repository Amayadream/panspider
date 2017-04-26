package com.amayadream.panspider.common.util;

import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @author :  Amayadream
 * @date :  2017.04.26 22:35
 */
public class ConfigUtils {

    private static final String configpath = "/config/dynamic.config.properties";

    private static Properties properties;
    private static long lasttime;

    /**
     * 获取配置文件的Properties,设置时间缓存
     * @return
     */
    private static Properties getProperties(){
        if (System.currentTimeMillis() - lasttime > 3600000 || properties == null) {
            InputStreamReader reader = null;
            properties = new Properties();
            try {
                reader = new InputStreamReader(ConfigUtils.class.getResourceAsStream(configpath), "utf-8");
                properties.load(reader);
                lasttime = System.currentTimeMillis();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return properties;
    }

    /**
     * 根据key获取配置
     * @param key
     * @return
     */
    public static String getValue(String key) {
        return getProperties().getProperty(key);
    }

}
