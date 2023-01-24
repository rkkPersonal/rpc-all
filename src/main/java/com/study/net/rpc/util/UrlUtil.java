package com.study.net.rpc.util;

import java.net.URI;

/**
 * @author Steven
 * @date 2023年01月22日 17:09
 */
public class UrlUtil {

    public static String getParam(String fieldName, URI uri) {
        String query = uri.getQuery();
        String[] split = query.split("&");
        for (String s : split) {
            if (s.startsWith(fieldName + "=")) {
                return s.replace(fieldName + "=", "");
            }
        }
        return null;
    }
}
