package com.dashboard.sharedboard.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

/**
 * Utility class for HTTP headers creation.
 */
public final class HeaderUtil {

    private static final String APP_NAME = "Shared Dashboard";

    private HeaderUtil() {
    }

    /**
     * <p>createAlert.</p>
     *
     * @param message a {@link java.lang.String} object.
     * @param param a {@link java.lang.String} object.
     * @return a {@link org.springframework.http.HttpHeaders} object.
     */
    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + APP_NAME + "-alert", message);
        headers.add("X-" + APP_NAME + "-params", param);
        return headers;
    }


    public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
        String message = "A new " + entityName + " is created with identifier " + param;
        return createAlert(message, param);
    }

    /**
     * <p>createEntityUpdateAlert.</p>
     *
     * @param entityName a {@link java.lang.String} object.
     * @param param a {@link java.lang.String} object.
     * @return a {@link org.springframework.http.HttpHeaders} object.
     */
    public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
        String message = "A " + entityName + " is updated with identifier " + param;
        return createAlert(message, param);
    }

    /**
     * <p>createEntityDeletionAlert.</p>
     *
     * @param entityName a {@link java.lang.String} object.
     * @param param a {@link java.lang.String} object.
     * @return a {@link org.springframework.http.HttpHeaders} object.
     */
    public static HttpHeaders createEntityDeletionAlert( String entityName, String param) {
        String message =  "A " + entityName + " is deleted with identifier " + param;
        return createAlert(message, param);
    }


}
