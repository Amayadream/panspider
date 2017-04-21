package com.amayadream.panspider.common.util;

import java.util.UUID;

/**
 * @author :  Amayadream
 * @date :  2017.04.18 22:12
 */
public class TokenGenerator {

    public static String getGuid(){
        return UUID.randomUUID().toString().trim().replaceAll("-", "").toLowerCase();
    }


}
