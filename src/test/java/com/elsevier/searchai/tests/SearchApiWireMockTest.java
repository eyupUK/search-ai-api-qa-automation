package com.elsevier.searchai.tests;

import com.elsevier.searchai.client.SearchApiClient;
import com.elsevier.searchai.config.RequestSpecFactory;
import com.elsevier.searchai.models.SearchResponse;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SearchApiWireMockTest {

    private static WireMockServer wireMockServer;
    private static SearchApiClient searchApiClient;

    @BeforeAll
    static void setUp() {

        wireMockServer = new WireMockServer(
                options().dynamicPort()
        );

        wireMockServer.start();

        wireMockServer.stubFor(
                get(urlPathEqualTo("/search"))
                        .withQueryParam(
                                "q",
                                equalTo("machine learning")
                        )
                        .withQueryParam(
                                "page",
                                equalTo("1")
                        )
                        .withQueryParam(
                                "pageSize",
                                equalTo("2")
                        )
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader(
                                                "Content-Type",
                                                "application/json"
                                        )
                                        .withBody("""
                                            {
                                              "query": "machine learning",
                                              "page": 1,
                                              "pageSize": 2,
                                              "totalResults": 2,
                                              "results": [
                                                {
                                                  "id": "DOC-001",
                                                  "title": "Machine Learning Research",
                                                  "abstract": "An overview of machine learning research.",
                                                  "authors": [
                                                    "Jane Smith",
                                                    "John Jones"
                                                  ],
                                                  "publicationYear": 2025,
                                                  "score": 0.98
                                                },
                                                {
                                                  "id": "DOC-002",
                                                  "title": "Deep Learning Methods",
                                                  "abstract": "A study of deep learning methods.",
                                                  "authors": [
                                                    "David Brown"
                                                  ],
                                                  "publicationYear": 2024,
                                                  "score": 0.91
                                                }
                                              ]
                                            }
                                            """)
                        )
        );

        searchApiClient = new SearchApiClient(
                RequestSpecFactory.defaultRequestSpec(
                        wireMockServer.baseUrl()
                )
        );
    }

    @AfterAll
    static void tearDown() {

        wireMockServer.stop();
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
                searchResponse.getResults().get(0).getTitle().isBlank()
        );

        assertEquals(
                2025,
                searchResponse.getResults().get(0).getPublicationYear()
        );
    }
}