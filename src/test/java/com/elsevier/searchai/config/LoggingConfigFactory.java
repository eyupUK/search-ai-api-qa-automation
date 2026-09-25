package com.elsevier.searchai.config;

import io.restassured.config.LogConfig;
import io.restassured.filter.log.LogDetail;

import static io.restassured.config.LogConfig.logConfig;

public final class LoggingConfigFactory {

    private static final String LOG_LEVEL_PROPERTY =
            "api.log.level";

    private static final String DEFAULT_LOG_LEVEL =
            "ALL";

    private LoggingConfigFactory() {
    }

    public static LogConfig create() {

        String logLevel = System.getProperty(
                LOG_LEVEL_PROPERTY,
                DEFAULT_LOG_LEVEL
        );

        LogConfig logConfig =
                logConfig()
                        .blacklistDefaultSensitiveHeaders();

        return switch (logLevel.toUpperCase()) {

            case "HEADERS" ->
                    logConfig.enableLoggingOfRequestAndResponseIfValidationFails(
                            LogDetail.HEADERS
                    );

            case "BODY" ->
                    logConfig.enableLoggingOfRequestAndResponseIfValidationFails(
                            LogDetail.BODY
                    );

            case "ALL" ->
                    logConfig.enableLoggingOfRequestAndResponseIfValidationFails();

            default ->
                    throw new IllegalArgumentException(
                            "Unsupported API log level: " + logLevel
                    );
        };
    }
}