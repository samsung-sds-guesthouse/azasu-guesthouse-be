package com.samsung.azasuguesthouse.common.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Log {
    private static final Log instance = new Log();

    private final Logger debugLogger = LoggerFactory.getLogger("debug");
    private final Logger infoLogger = LoggerFactory.getLogger("info");
    private final Logger errorLogger = LoggerFactory.getLogger("error");

    public static void debug(String msg) {
        instance.debugLogger.debug(msg);
    }
    public static void info(String msg) {
        instance.infoLogger.info(msg);
    }
    public static void error(String msg, Throwable t) {
        instance.errorLogger.error(msg, t);
    }
    public static void error(String msg) {
        instance.errorLogger.error(msg);
    }

    // request
    private final Logger requestLogger = LoggerFactory.getLogger("request");

    public static void request(int httpStatus, long memberId, String msg, String uri, Map<String, String[]> paramMap, long elapsedTime) {
        if (msg == null) {
            msg = "-";
        } else {
            msg = msg.replace(' ', '_').replace('\n', '_').replace('\r', '_');
        }

        String queryString = "";
        if (paramMap != null && !paramMap.isEmpty()) {
            queryString = paramMap
                    .entrySet()
                    .stream()
                    .map(e ->
                            Arrays.stream(e.getValue())
                                    .map(e2 -> e.getKey() + "=" + e2)
                                    .collect(Collectors.joining("&")))
                    .collect(Collectors.joining("&"));
        }

        instance.requestLogger.info(
                "{} {} {} {}?{} {}",
                httpStatus,
                memberId == 0 ? "-" : memberId,
                msg,
                uri == null ? "/" : uri,
                queryString,
                elapsedTime
        );
    }
}
