package com.elsevier.searchai.config;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;

public final class ResponseSpecFactory {

    private ResponseSpecFactory() {
    }

    public static ResponseSpecification successfulJsonResponse() {

        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .build();
    }
}