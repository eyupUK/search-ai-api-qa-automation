package com.elsevier.searchai.config;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public final class RequestSpecFactory {

    private static final String DEFAULT_BASE_URI =
            "https://jsonplaceholder.typicode.com";

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
}