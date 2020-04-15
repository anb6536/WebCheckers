package com.webcheckers.util;

import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

public class Serializer {
    public static String serialize(Map<String, String> objectMap) {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (Entry<String, String> ent : objectMap.entrySet()) {
            try {
                builder.append(URLEncoder.encode(ent.getKey(), "UTF-8"));
                builder.append("=");
                builder.append(URLEncoder.encode(ent.getValue(), "UTF-8"));
                i++;
                if (i < objectMap.size()) {
                    builder.append("&");
                }
            } catch (Exception e) {

            }
        }
        return builder.toString();
    }
}