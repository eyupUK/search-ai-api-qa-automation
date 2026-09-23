package com.elsevier.searchai.tests;

import com.elsevier.searchai.assertions.ApiErrorAssertions;
import com.elsevier.searchai.client.SearchApiClient;
import com.elsevier.searchai.config.RequestSpecFactory;
import com.elsevier.searchai.models.SearchResponse;
import com.elsevier.searchai.stubs.SearchApiStubs;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.jupiter.api.Assertions.*;

class SearchApiWireMockTest {

    private static final String VALID_TOKEN = "valid-token";

    private static final String FORBIDDEN_TOKEN =
            "forbidden-token";

    private static final String RATE_LIMITED_TOKEN =
            "rate-limited-token";

    private static final String SERVER_ERROR_TOKEN =
            "server-error-token";

    private static final String INVALID_CONTRACT_TOKEN =
            "invalid-contract-token";

    private static final String MISSING_QUERY_TOKEN =
            "missing-query-token";

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

        SearchApiStubs.stubSuccessfulSearch(
                wireMockServer
        );

        SearchApiStubs.stubBlankQueryBadRequest(
                wireMockServer
        );

        SearchApiStubs.stubMissingAuthentication(
                wireMockServer
        );

        SearchApiStubs.stubForbidden(
                wireMockServer
        );

        SearchApiStubs.stubRateLimited(
                wireMockServer
        );

        SearchApiStubs.stubServerError(
                wireMockServer
        );

        SearchApiStubs.stubInvalidSearchContract(
                wireMockServer
        );

        SearchApiStubs.stubMissingQueryBadRequest(
                wireMockServer
        );

        SearchApiStubs.stubInvalidPageBadRequest(
                wireMockServer
        );

        SearchApiStubs.stubInvalidPageSizeBadRequest(
                wireMockServer
        );

        SearchApiStubs.stubInvalidPageFormat(
                wireMockServer
        );

        SearchApiStubs.stubInvalidPageSizeFormat(
                wireMockServer
        );

        SearchApiStubs.stubEmptyPage(
                wireMockServer
        );

        SearchApiStubs.stubEmptyPageSize(
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
                2,
                VALID_TOKEN
        );

        response.then()
                .log().all()
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
                searchResponse.getResults()
                        .get(0)
                        .getId()
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

    @ParameterizedTest
    @ValueSource(ints = {1, 50, 100})
    void shouldReturnSearchResultsDD(int pageSize) {

        Response response = searchApiClient.search(
                "machine learning",
                1,
                pageSize,
                VALID_TOKEN
        );

        response.then()
                .log().all()
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
                searchResponse.getResults()
                        .get(0)
                        .getId()
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
                2,
                VALID_TOKEN
        );

        ApiErrorAssertions.assertError(
                response,
                400,
                "INVALID_QUERY",
                "Query parameter 'q' must not be blank"
        );
    }

    @Test
    void shouldReturnUnauthorizedWhenAuthenticationIsMissing() {

        Response response = searchApiClient.search(
                "machine learning",
                1,
                2
        );

        ApiErrorAssertions.assertError(
                response,
                401,
                "UNAUTHORIZED",
                "Authentication is required"
        );
    }

    @Test
    void shouldReturnForbiddenWhenUserHasInsufficientPermissions() {

        Response response = searchApiClient.search(
                "machine learning",
                1,
                2,
                FORBIDDEN_TOKEN
        );

        ApiErrorAssertions.assertError(
                response,
                403,
                "FORBIDDEN",
                "Insufficient permissions"
        );
    }

    @Test
    void shouldReturnTooManyRequestsWhenRateLimitIsExceeded() {

        Response response = searchApiClient.search(
                "machine learning",
                1,
                2,
                RATE_LIMITED_TOKEN
        );

        ApiErrorAssertions.assertError(
                response,
                429,
                "RATE_LIMITED",
                "Rate limit exceeded"
        );

        assertEquals(
                "30",
                response.getHeader("Retry-After")
        );
    }

    @Test
    void shouldReturnServerErrorWhenSearchDependencyFails() {

        Response response = searchApiClient.search(
                "machine learning",
                1,
                2,
                SERVER_ERROR_TOKEN
        );

        ApiErrorAssertions.assertError(
                response,
                500,
                "DOWNSTREAM_ERROR",
                "Search dependency is unavailable"
        );
    }

    @Test
    void shouldMatchSearchResponseSchema() {

        Response response = searchApiClient.search(
                "machine learning",
                1,
                2,
                VALID_TOKEN
        );

        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body(
                        matchesJsonSchemaInClasspath(
                                "schemas/search-response-schema.json"
                        )
                );
    }

    @Test
    void shouldRejectInvalidSearchResponseContract() {

        Response response = searchApiClient.search(
                "machine learning",
                1,
                2,
                INVALID_CONTRACT_TOKEN
        );

        assertThrows(
                AssertionError.class,
                () -> response.then()
                        .body(
                                matchesJsonSchemaInClasspath(
                                        "schemas/search-response-schema.json"
                                )
                        )
        );
    }

    @Test
    void shouldReturnBadRequestWhenQueryIsMissing() {

        Response response = searchApiClient.searchWithoutQuery(
                1,
                2,
                MISSING_QUERY_TOKEN
        );

        ApiErrorAssertions.assertError(
                response,
                400,
                "MISSING_QUERY",
                "Query parameter 'q' is required"
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void shouldReturnBadRequestForInvalidPage(int page) {

        Response response = searchApiClient.search(
                "machine learning",
                page,
                2,
                VALID_TOKEN
        );

        ApiErrorAssertions.assertError(
                response,
                400,
                "INVALID_PAGE",
                "Page must be greater than or equal to 1"
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 101})
    void shouldReturnBadRequestForInvalidPageSize(int pageSize) {

        Response response = searchApiClient.search(
                "machine learning",
                1,
                pageSize,
                VALID_TOKEN
        );

        ApiErrorAssertions.assertError(
                response,
                400,
                "INVALID_PAGE_SIZE",
                "Page size must be between 1 and 100"
        );
    }

    @Test
    void shouldReturnBadRequestWhenPageIsNotAnInteger() {

        Response response = searchApiClient.searchRaw(
                "machine learning",
                "abc",
                "2",
                VALID_TOKEN
        );

        ApiErrorAssertions.assertError(
                response,
                400,
                "INVALID_PAGE",
                "Page must be a valid integer"
        );
    }

    @Test
    void shouldReturnBadRequestWhenPageIsEmpty() {

        Response response = searchApiClient.searchRaw(
                "machine learning",
                "",
                "2",
                VALID_TOKEN
        );

        ApiErrorAssertions.assertError(
                response,
                400,
                "INVALID_PAGE",
                "Page must be a valid integer"
        );
    }

    @Test
    void shouldReturnBadRequestWhenPageSizeIsNotAnInteger() {

        Response response = searchApiClient.searchRaw(
                "machine learning",
                "1",
                "abc",
                VALID_TOKEN
        );

        ApiErrorAssertions.assertError(
                response,
                400,
                "INVALID_PAGE_SIZE",
                "Page size must be a valid integer"
        );
    }

    @Test
    void shouldReturnBadRequestWhenPageSizeIsEmpty() {

        Response response = searchApiClient.searchRaw(
                "machine learning",
                "1",
                "",
                VALID_TOKEN
        );

        ApiErrorAssertions.assertError(
                response,
                400,
                "INVALID_PAGE_SIZE",
                "Page size must be a valid integer"
        );
    }

}