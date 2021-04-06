package com.dashboard.sharedboard.util;

import org.springframework.http.MediaType;

/**
 * Utility class for testing REST controllers.
 */
public final class TestUtil {

    /**
     * MediaType for JSON UTF8
     */
    public static final MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON;

    private TestUtil() {
    }

    public static String randomStr(int maxVal) {
        return String.valueOf((int) Math.floor(Math.random() * maxVal));
    }

    public static int randomInt(int maxVal) {
        return (int) Math.floor(Math.random() * maxVal);
    }


}
