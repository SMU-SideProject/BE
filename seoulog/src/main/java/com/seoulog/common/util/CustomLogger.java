package com.seoulog.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomLogger {

    private static Logger log;

    public Logger getLog() {
        return LoggerFactory.getLogger(this.getClass());
    }

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
