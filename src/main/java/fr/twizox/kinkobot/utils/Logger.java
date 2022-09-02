package fr.twizox.kinkobot.utils;

import org.slf4j.LoggerFactory;

public class Logger {

    public static void error(Class<?> clazz, String message) {
        LoggerFactory.getLogger(clazz).error(message);
    }

    public static void info(Class<?> clazz, String message) {
        LoggerFactory.getLogger(clazz).info(message);
    }

    public static void warn(Class<?> clazz, String message) {
        LoggerFactory.getLogger(clazz).warn(message);
    }

}
