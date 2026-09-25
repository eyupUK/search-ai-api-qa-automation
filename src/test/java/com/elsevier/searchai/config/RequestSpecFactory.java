package com.elsevier.searchai.config;

import com.elsevier.searchai.auth.TokenProvider;
import com.elsevier.searchai.filters.CorrelationIdFilter;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import static io.restassured.config.RestAssuredConfig.config;

public final class RequestSpecFactory {

    private static final String DEFAULT_BASE_URI =
            "https://jsonplaceholder.typicode.com";

    private static final String AUTHORIZATION_HEADER =
            "Authorization";

    private static final String BEARER_PREFIX =
            "Bearer ";

    private static final String CORRELATION_ID_HEADER =
            "X-Correlation-ID";

    private RequestSpecFactory() {
    }

    public static RequestSpecification defaultRequestSpec() {

        String baseUri = System.getProperty(
                "baseUri",
                DEFAULT_BASE_URI
        );

        return defaultRequestSpec(baseUri);
    }

    public static RequestSpecification defaultRequestSpec(
            String baseUri
    ) {

        RestAssuredConfig restAssuredConfig =
                config()
                        .logConfig(
                                LoggingConfigFactory.create()
                        );

        return new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setAccept(ContentType.JSON)
                .setConfig(restAssuredConfig)
                .addFilter(new CorrelationIdFilter())
                .build();
    }

    public static RequestSpecification authenticatedRequestSpec(
            RequestSpecification baseRequestSpec,
            TokenProvider tokenProvider
    ) {

        if (tokenProvider == null) {
            throw new IllegalArgumentException(
                    "Token provider must not be null"
            );
        }

        String accessToken =
                tokenProvider.getAccessToken();

        return new RequestSpecBuilder()
                .addRequestSpecification(baseRequestSpec)
                .addHeader(
                        AUTHORIZATION_HEADER,
                        BEARER_PREFIX + accessToken
                )
                .build();
    }
}