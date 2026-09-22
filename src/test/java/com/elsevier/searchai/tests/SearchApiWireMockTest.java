package com.elsevier.searchai.tests;

import com.elsevier.searchai.client.SearchApiClient;
import com.elsevier.searchai.config.RequestSpecFactory;
import com.elsevier.searchai.models.SearchResponse;
import com.elsevier.searchai.stubs.SearchApiStubs;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SearchApiWireMockTest {

    private static WireMockServer wireMockServer;
    private static SearchApiClient searchApiClient;

    @BeforeAll
    static void setUp() {

        wireMockServer = new WireMockServer(
                options()
                        .dynamicPort()
                        .usingFilesUnderDirectory(
                                "src/test/resources/wiremock"
                        )
        );

        wireMockServer.start();

        SearchApiStubs.stubSuccessfulSearch(wireMockServer);

        SearchApiStubs.stubBlankQueryBadRequest(
                wireMockServer
        );

        searchApiClient = new SearchApiClient(
                RequestSpecFactory.defaultRequestSpec(
                        wireMockServer.baseUrl()
                )
        );
    }

    @AfterAll
    static void tearDown() {

        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @Test
    void shouldReturnSearchResults() {

        Response response = searchApiClient.search(
                "machine learning",
                1,
                2
        );

        response.then()
                .statusCode(200)
                .contentType("application/json");

        SearchResponse searchResponse =
                response.as(SearchResponse.class);

        assertEquals(
                "machine learning",
                searchResponse.getQuery()
        );

        assertEquals(
                1,
                searchResponse.getPage()
        );

        assertEquals(
                2,
                searchResponse.getPageSize()
        );

        assertEquals(
                2,
                searchResponse.getTotalResults()
        );

        assertNotNull(searchResponse.getResults());

        assertEquals(
                2,
                searchResponse.getResults().size()
        );

        assertEquals(
                "DOC-001",
                searchResponse.getResults().get(0).getId()
        );

        assertFalse(
                searchResponse.getResults()
                        .get(0)
                        .getTitle()
                        .isBlank()
        );

        assertEquals(
                2025,
                searchResponse.getResults()
                        .get(0)
                        .getPublicationYear()
        );
    }

    @Test
    void shouldReturnBadRequestWhenQueryIsBlank() {

        Response response = searchApiClient.search(
                "",
                1,
                2
        );

        response.then()
                .statusCode(400)
                .contentType("application/json")
                .body(
                        "error",
                        equalTo("INVALID_QUERY")
                )
                .body(
                        "message",
                        equalTo(
                                "Query parameter 'q' must not be blank"
                        )
                );
    }
}