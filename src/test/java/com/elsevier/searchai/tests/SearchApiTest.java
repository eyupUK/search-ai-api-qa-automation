package com.elsevier.searchai.tests;

import com.elsevier.searchai.client.SearchApiClient;
import com.elsevier.searchai.config.RequestSpecFactory;
import com.elsevier.searchai.models.Post;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;

class SearchApiTest {

    private SearchApiClient searchApiClient;

    @BeforeEach
    void setUp() {
        searchApiClient = new SearchApiClient(
                RequestSpecFactory.defaultRequestSpec()
        );
    }

    @Test
    void shouldReturnPostById() {

        Response response = searchApiClient.getPostById(1);

        response
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("id", equalTo(1))
                .body("userId", notNullValue())
                .body("title", notNullValue())
                .body("body", notNullValue());
    }

    @Test
    void shouldReturnNotFoundForUnknownPost() {

        Response response = searchApiClient.getPostById(99999);

        response
                .then()
                .statusCode(404);
    }

    @Test
    void shouldReturnCollectionOfPosts() {

        Response response = searchApiClient.getAllPosts();

        response
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("$", hasSize(greaterThan(0)))
                .body("id", hasItem(1))
                .body("userId", notNullValue())
                .body("title", notNullValue())
                .body("body", notNullValue());
    }

    @Test
    void shouldReturnPostsForSpecificUser() {

        Response response = searchApiClient.getPostsByUserId(1);

        response
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("$", hasSize(greaterThan(0)))
                .body("userId", everyItem(equalTo(1)));
    }

    @Test
    void shouldDeserializePostResponse() {

        Response response = searchApiClient.getPostById(1);

        response.then()
                .statusCode(200)
                .contentType("application/json");

        Post post = response.as(Post.class);

        assertEquals(1, post.getId());
        assertEquals(1, post.getUserId());
        assertNotNull(post.getTitle());
        assertFalse(post.getTitle().isBlank());
        assertNotNull(post.getBody());
        assertFalse(post.getBody().isBlank());
    }
}