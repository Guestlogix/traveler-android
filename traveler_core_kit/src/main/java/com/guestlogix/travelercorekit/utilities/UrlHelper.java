package com.guestlogix.travelercorekit.utilities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public class UrlHelper {
    public static String urlEncodeUTF8(Map<?, ?> map) {
        if (null == map)
            return "";
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            if (entry.getValue() instanceof List) {
                String key = entry.getKey().toString();
                List values = (List) entry.getValue();
                sb.append(urlEncodeUTF8(values, key));
            } else {
                sb.append(String.format("%s=%s",
                        urlEncodeUTF8(entry.getKey().toString()),
                        urlEncodeUTF8(entry.getValue().toString())
                ));
            }
        }
        return sb.toString();
    }

    public static String urlEncodeUTF8(List values, String key) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0 ; i < values.size(); i++) {
            sb.append(String.format("%s=%s",
                    urlEncodeUTF8(key),
                    urlEncodeUTF8(values.get(i).toString())
            ));

            if (i < values.size() - 1) {
                sb.append('&');
            }
        }

        return sb.toString();
    }

    public static String urlEncodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }
}
