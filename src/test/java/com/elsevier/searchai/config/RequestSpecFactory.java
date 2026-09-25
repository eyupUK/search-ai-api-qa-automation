package com.elsevier.searchai.config;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public final class RequestSpecFactory {

    private static final String DEFAULT_BASE_URI =
            "https://jsonplaceholder.typicode.com";

    private static final String AUTHORIZATION_HEADER =
            "Authorization";

    private static final String BEARER_PREFIX =
            "Bearer ";

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

        return new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setAccept(ContentType.JSON)
                .build();
    }

    public static RequestSpecification authenticatedRequestSpec(
            RequestSpecification baseRequestSpec,
            String accessToken
    ) {

        if (accessToken == null || accessToken.isBlank()) {
            throw new IllegalArgumentException(
                    "Access token must not be null or blank"
            );
        }

        return new RequestSpecBuilder()
                .addRequestSpecification(baseRequestSpec)
                .addHeader(
                        AUTHORIZATION_HEADER,
                        BEARER_PREFIX + accessToken
                )
                .build();
    }
}