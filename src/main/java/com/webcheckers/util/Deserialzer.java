package com.webcheckers.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Deserializes form-url-encoded data to a Map of Key-values
 */
public class Deserialzer {
    /**
     * Deserialized a given requestbody into the information contained
     *
     * @param information A string that is xxx-formurl-encoded data
     * @return A map of the key value pairs
     */
    public static Map<String, String> deserialize(String information) {
        Map<String, String> urlParameters = new HashMap<String, String>();
        if (information == null)
            return urlParameters;
        String[] kvPairs = information.split("&");
        for (String str : kvPairs) {
            String[] splitted = str.split("=");
            try {
                urlParameters.put(splitted[0], URLDecoder.decode(splitted[1], "UTF-8"));
            } catch (UnsupportedEncodingException e) {

            }
        }
        return urlParameters;
    }

}