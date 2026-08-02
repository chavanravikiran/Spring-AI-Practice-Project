package com.example.jobagent.util;

public final class JsonUtil {

    private JsonUtil() {
    }

    public static String extractJson(String text) {
        if (text == null) {
            return null;
        }
        int start = text.indexOf('{');
        int end = text.lastIndexOf('}');
        if (start == -1 || end == -1 || end < start) {
            return text.trim();
        }
        return text.substring(start, end + 1);
    }
}