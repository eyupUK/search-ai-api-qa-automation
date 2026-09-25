package com.elsevier.searchai.tests;

import com.elsevier.searchai.assertions.ApiErrorAssertions;
import com.elsevier.searchai.client.SearchApiClient;
import com.elsevier.searchai.config.RequestSpecFactory;
import com.elsevier.searchai.config.ResponseSpecFactory;
import com.elsevier.searchai.models.SearchRequest;
import com.elsevier.searchai.models.SearchResponse;
import com.elsevier.searchai.stubs.SearchApiStubs;
import com.elsevier.searchai.testdata.SearchTestData;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import io.restassured.specification.RequestSpecification;

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

    private static final String SEARCH_QUERY =
            "machine learning";

    private static final int FIRST_PAGE = 1;

    private static final int DEFAULT_PAGE_SIZE = 2;

    private static WireMockServer wireMockServer;

    private static RequestSpecification baseRequestSpec;

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

        baseRequestSpec =
                RequestSpecFactory.defaultRequestSpec(
                        wireMockServer.baseUrl()
                );

        searchApiClient =
                new SearchApiClient(baseRequestSpec);
    }

    @BeforeEach
    void resetWireMock() {
        wireMockServer.resetAll();
    }

    @AfterAll
    static void tearDown() {

        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    private SearchApiClient authenticatedClient(
            String accessToken
    ) {

        RequestSpecification authenticatedRequestSpec =
                RequestSpecFactory.authenticatedRequestSpec(
                        baseRequestSpec,
                        accessToken
                );

        return new SearchApiClient(
                authenticatedRequestSpec
        );
    }

    @Test
    void shouldReturnSearchResults() {

        SearchRequest searchRequest =
                SearchTestData.validSearch()
                        .build();

        SearchApiStubs.stubSearchResponse(
                wireMockServer,
                VALID_TOKEN,
                searchRequest.getQuery(),
                String.valueOf(searchRequest.getPage()),
                String.valueOf(searchRequest.getPageSize()),
                200,
                "search/success.json"
        );

        Response response =
                authenticatedClient(VALID_TOKEN)
                        .search(searchRequest);

        response.then()
                .spec(
                        ResponseSpecFactory.successfulJsonResponse()
                )
                .body(
                        "query",
                        equalTo(SEARCH_QUERY)
                );

        SearchResponse searchResponse =
                response.as(SearchResponse.class);

        assertEquals(
                SEARCH_QUERY,
                searchResponse.getQuery()
        );

        assertEquals(
                FIRST_PAGE,
                searchResponse.getPage()
        );

        assertEquals(
                DEFAULT_PAGE_SIZE,
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

        assertEquals(
                searchRequest.getQuery(),
                searchResponse.getQuery()
        );

        wireMockServer.verify(
                1,
                getRequestedFor(
                        urlPathEqualTo("/search")
                ).withHeader(
                        "X-Correlation-ID",
                        matching(
                                "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}"
                        )
                )
        );
    }

    @Test
    void shouldReturnSearchResultsUsingSearchRequest() {

        SearchRequest searchRequest = SearchRequest.builder()
                .query(SEARCH_QUERY)
                .page(FIRST_PAGE)
                .pageSize(DEFAULT_PAGE_SIZE)
                .build();

        SearchApiStubs.stubSearchResponse(
                wireMockServer,
                VALID_TOKEN,
                SEARCH_QUERY,
                String.valueOf(FIRST_PAGE),
                String.valueOf(DEFAULT_PAGE_SIZE),
                200,
                "search/success.json"
        );

        Response response =
                authenticatedClient(VALID_TOKEN)
                        .search(
                                searchRequest
                        );

        response.then()
                .statusCode(200)
                .contentType("application/json");

        SearchResponse searchResponse =
                response.as(SearchResponse.class);

        assertEquals(
                SEARCH_QUERY,
                searchResponse.getQuery()
        );

        assertEquals(
                FIRST_PAGE,
                searchResponse.getPage()
        );

        assertEquals(
                DEFAULT_PAGE_SIZE,
                searchResponse.getPageSize()
        );
    }

    @Test
    void shouldReturnBadRequestWhenQueryIsBlank() {

        SearchApiStubs.stubSearchResponse(
                wireMockServer,
                VALID_TOKEN,
                "",
                String.valueOf(FIRST_PAGE),
                String.valueOf(DEFAULT_PAGE_SIZE),
                400,
                "search/bad-request.json"
        );

        SearchRequest searchRequest = SearchRequest.builder()
                .query("")
                .page(FIRST_PAGE)
                .pageSize(DEFAULT_PAGE_SIZE)
                .build();

        Response response =
                authenticatedClient(VALID_TOKEN)
                        .search(
                                "",
                                FIRST_PAGE,
                                DEFAULT_PAGE_SIZE
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

        SearchApiStubs.stubSearchWithoutAuthentication(
                wireMockServer,
                SEARCH_QUERY,
                String.valueOf(FIRST_PAGE),
                String.valueOf(DEFAULT_PAGE_SIZE),
                401,
                "search/unauthorized.json"
        );

        Response response = searchApiClient.search(
                SEARCH_QUERY,
                FIRST_PAGE,
                DEFAULT_PAGE_SIZE
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

        SearchApiStubs.stubSearchResponse(
                wireMockServer,
                FORBIDDEN_TOKEN,
                SEARCH_QUERY,
                String.valueOf(FIRST_PAGE),
                String.valueOf(DEFAULT_PAGE_SIZE),
                403,
                "search/forbidden.json"
        );

        SearchRequest searchRequest = SearchRequest.builder()
                .query(SEARCH_QUERY)
                .page(FIRST_PAGE)
                .pageSize(DEFAULT_PAGE_SIZE)
                .build();

        Response response =
                authenticatedClient(FORBIDDEN_TOKEN)
                        .search(
                                searchRequest
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

        SearchApiStubs.stubRateLimitedSearch(
                wireMockServer,
                RATE_LIMITED_TOKEN,
                SEARCH_QUERY,
                String.valueOf(FIRST_PAGE),
                String.valueOf(DEFAULT_PAGE_SIZE),
                429,
                "search/rate-limited.json",
                "30"
        );

        SearchRequest searchRequest = SearchRequest.builder()
                .query(SEARCH_QUERY)
                .page(FIRST_PAGE)
                .pageSize(DEFAULT_PAGE_SIZE)
                .build();

        Response response =
                authenticatedClient(RATE_LIMITED_TOKEN)
                        .search(
                                searchRequest
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

        SearchApiStubs.stubSearchResponse(
                wireMockServer,
                SERVER_ERROR_TOKEN,
                SEARCH_QUERY,
                String.valueOf(FIRST_PAGE),
                String.valueOf(DEFAULT_PAGE_SIZE),
                500,
                "search/server-error.json"
        );

        SearchRequest searchRequest = SearchRequest.builder()
                .query(SEARCH_QUERY)
                .page(FIRST_PAGE)
                .pageSize(DEFAULT_PAGE_SIZE)
                .build();

        Response response =
                authenticatedClient(SERVER_ERROR_TOKEN)
                        .search(
                                searchRequest
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

        SearchApiStubs.stubSearchResponse(
                wireMockServer,
                VALID_TOKEN,
                SEARCH_QUERY,
                String.valueOf(FIRST_PAGE),
                String.valueOf(DEFAULT_PAGE_SIZE),
                200,
                "search/success.json"
        );

        SearchRequest searchRequest = SearchRequest.builder()
                .query(SEARCH_QUERY)
                .page(FIRST_PAGE)
                .pageSize(DEFAULT_PAGE_SIZE)
                .build();

        Response response =
                authenticatedClient(VALID_TOKEN)
                        .search(searchRequest);

        response.then()
                .spec(
                        ResponseSpecFactory.successfulJsonResponse()
                )
                .body(
                        matchesJsonSchemaInClasspath(
                                "schemas/search-response-schema.json"
                        )
                );
    }

    @Test
    void shouldRejectInvalidSearchResponseContract() {

        SearchApiStubs.stubSearchResponse(
                wireMockServer,
                INVALID_CONTRACT_TOKEN,
                SEARCH_QUERY,
                String.valueOf(FIRST_PAGE),
                String.valueOf(DEFAULT_PAGE_SIZE),
                200,
                "search/invalid-contract.json"
        );

        SearchRequest searchRequest = SearchRequest.builder()
                .query(SEARCH_QUERY)
                .page(FIRST_PAGE)
                .pageSize(DEFAULT_PAGE_SIZE)
                .build();

        Response response =
                authenticatedClient(INVALID_CONTRACT_TOKEN)
                        .search(
                                searchRequest
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

        SearchApiStubs.stubSearchWithoutQuery(
                wireMockServer,
                VALID_TOKEN,
                String.valueOf(FIRST_PAGE),
                String.valueOf(DEFAULT_PAGE_SIZE),
                400,
                "search/missing-query.json"
        );

        Response response =
                authenticatedClient(VALID_TOKEN)
                        .searchWithoutQuery(
                                FIRST_PAGE,
                                DEFAULT_PAGE_SIZE
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

        SearchRequest request =
                SearchTestData.validSearch()
                        .page(page)
                        .build();

        SearchApiStubs.stubSearchResponse(
                wireMockServer,
                VALID_TOKEN,
                request.getQuery(),
                String.valueOf(request.getPage()),
                String.valueOf(request.getPageSize()),
                400,
                "search/invalid-page.json"
        );

        Response response =
                authenticatedClient(VALID_TOKEN)
                        .search(
                                request
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

        SearchApiStubs.stubSearchResponse(
                wireMockServer,
                VALID_TOKEN,
                SEARCH_QUERY,
                String.valueOf(FIRST_PAGE),
                String.valueOf(pageSize),
                400,
                "search/invalid-page-size.json"
        );

        SearchRequest searchRequest = SearchRequest.builder()
                .query(SEARCH_QUERY)
                .page(FIRST_PAGE)
                .pageSize(pageSize)
                .build();

        Response response =
                authenticatedClient(VALID_TOKEN)
                        .search(
                                searchRequest
                        );

        ApiErrorAssertions.assertError(
                response,
                400,
                "INVALID_PAGE_SIZE",
                "Page size must be between 1 and 100"
        );
    }

    // Malformed request tests
    @Test
    void shouldReturnBadRequestWhenPageIsNotAnInteger() {

        SearchApiStubs.stubSearchResponse(
                wireMockServer,
                VALID_TOKEN,
                SEARCH_QUERY,
                "abc",
                String.valueOf(DEFAULT_PAGE_SIZE),
                400,
                "search/invalid-page-format.json"
        );

        Response response =
                authenticatedClient(VALID_TOKEN)
                        .searchRaw(
                                SEARCH_QUERY,
                                "abc",
                                String.valueOf(DEFAULT_PAGE_SIZE)
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

        SearchApiStubs.stubSearchResponse(
                wireMockServer,
                VALID_TOKEN,
                SEARCH_QUERY,
                "",
                String.valueOf(DEFAULT_PAGE_SIZE),
                400,
                "search/invalid-page-format.json"
        );

        Response response =
                authenticatedClient(VALID_TOKEN)
                        .searchRaw(
                                SEARCH_QUERY,
                                "",
                                String.valueOf(DEFAULT_PAGE_SIZE)
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

        SearchApiStubs.stubSearchResponse(
                wireMockServer,
                VALID_TOKEN,
                SEARCH_QUERY,
                String.valueOf(FIRST_PAGE),
                "abc",
                400,
                "search/invalid-page-size-format.json"
        );

        Response response =
                authenticatedClient(VALID_TOKEN)
                        .searchRaw(
                                SEARCH_QUERY,
                                String.valueOf(FIRST_PAGE),
                                "abc"
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

        SearchApiStubs.stubSearchResponse(
                wireMockServer,
                VALID_TOKEN,
                SEARCH_QUERY,
                String.valueOf(FIRST_PAGE),
                "",
                400,
                "search/invalid-page-size-format.json"
        );

        Response response =
                authenticatedClient(VALID_TOKEN)
                        .searchRaw(
                                SEARCH_QUERY,
                                String.valueOf(FIRST_PAGE),
                                ""
                        );

        ApiErrorAssertions.assertError(
                response,
                400,
                "INVALID_PAGE_SIZE",
                "Page size must be a valid integer"
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "hello", "1.5"})
    void shouldReturnBadRequestWhenPageFormatIsInvalid(
            String page
    ) {

        SearchApiStubs.stubSearchResponse(
                wireMockServer,
                VALID_TOKEN,
                SEARCH_QUERY,
                page,
                String.valueOf(DEFAULT_PAGE_SIZE),
                400,
                "search/invalid-page-format.json"
        );

        Response response =
                authenticatedClient(VALID_TOKEN)
                        .searchRaw(
                                SEARCH_QUERY,
                                page,
                                String.valueOf(DEFAULT_PAGE_SIZE)
                        );

        ApiErrorAssertions.assertError(
                response,
                400,
                "INVALID_PAGE",
                "Page must be a valid integer"
        );
    }
}