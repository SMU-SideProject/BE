package com.seoulog.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomLogger {

    private static final Logger log = LoggerFactory.getLogger(Logger.class);

    public static void error(String message) {
        log.error(message);
    }

    public static void info(String message) {
        log.info(message);
    }

    public static void trace(String message) {
        log.trace(message);
    }
}
