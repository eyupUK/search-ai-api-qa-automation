package com.elsevier.searchai.client;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class SearchApiClient {

    private final RequestSpecification requestSpec;

    public SearchApiClient(RequestSpecification requestSpec) {
        this.requestSpec = requestSpec;
    }

    public Response getPostById(int postId) {

        return given()
                .spec(requestSpec)
                .when()
                .get("/posts/{id}", postId);
    }

    public Response getAllPosts() {

        return given()
                .spec(requestSpec)
                .when()
                .get("/posts");
    }

    public Response getPostsByUserId(int userId) {

        return given()
                .spec(requestSpec)
                .queryParam("userId", userId)
                .when()
                .get("/posts");
    }

    public Response search(
            String query,
            int page,
            int pageSize
    ) {

        return search(
                query,
                page,
                pageSize,
                null
        );
    }

    public Response search(
            String query,
            int page,
            int pageSize,
            String accessToken
    ) {

        RequestSpecification request =
                given()
                        .spec(requestSpec)
                        .queryParam("q", query)
                        .queryParam("page", page)
                        .queryParam("pageSize", pageSize);

        if (accessToken != null && !accessToken.isBlank()) {
            request.header(
                    "Authorization",
                    "Bearer " + accessToken
            );
        }
        System.out.println("Request: " + request.log().all());

        return request
                .when()
                .get("/search");
    }

    public Response searchWithoutQuery(
            int page,
            int pageSize,
            String accessToken
    ) {

        RequestSpecification request =
                given()
                        .spec(requestSpec)
                        .queryParam("page", page)
                        .queryParam("pageSize", pageSize);

        if (accessToken != null && !accessToken.isBlank()) {
            request.header(
                    "Authorization",
                    "Bearer " + accessToken
            );
        }
        System.out.println("Request: " + request.log().all());
        return request
                .when()
                .get("/search");
    }
}