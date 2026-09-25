package com.elsevier.searchai.client;

import com.elsevier.searchai.config.RequestSpecFactory;
import com.elsevier.searchai.models.SearchRequest;
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
                SearchRequest.builder()
                        .query(query)
                        .page(page)
                        .pageSize(pageSize)
                        .build(),
                null
        );
    }

    public Response search(
            String query,
            int page,
            int pageSize,
            String accessToken
    ) {

        return search(
                SearchRequest.builder()
                        .query(query)
                        .page(page)
                        .pageSize(pageSize)
                        .build(),
                accessToken
        );
    }

    public Response search(
            SearchRequest searchRequest,
            String accessToken
    ) {

        RequestSpecification request =
                requestWithAuthentication(accessToken)
                        .queryParam(
                                "q",
                                searchRequest.getQuery()
                        )
                        .queryParam(
                                "page",
                                searchRequest.getPage()
                        )
                        .queryParam(
                                "pageSize",
                                searchRequest.getPageSize()
                        );

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
                requestWithAuthentication(accessToken)
                        .queryParam("page", page)
                        .queryParam("pageSize", pageSize);

        return request
                .when()
                .get("/search");
    }

    public Response searchRaw(
            String query,
            String page,
            String pageSize,
            String accessToken
    ) {

        RequestSpecification request =
                requestWithAuthentication(accessToken)
                        .queryParam("q", query)
                        .queryParam("page", page)
                        .queryParam("pageSize", pageSize);

        return request
                .when()
                .get("/search");
    }

    private RequestSpecification requestWithAuthentication(
            String accessToken
    ) {

        if (accessToken == null || accessToken.isBlank()) {
            return given()
                    .spec(requestSpec);
        }

        return given()
                .spec(
                        RequestSpecFactory.authenticatedRequestSpec(
                                requestSpec,
                                accessToken
                        )
                );
    }
}