package com.elsevier.searchai.stubs;

import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public final class SearchApiStubs {

    private SearchApiStubs() {
    }

    public static void stubSuccessfulSearch(
            WireMockServer wireMockServer
    ) {
        System.out.println("Stubbing successful search response for /search endpoint");

        wireMockServer.stubFor(
                get(urlPathEqualTo("/search"))
                        .withHeader(
                                "Authorization",
                                equalTo("Bearer valid-token")
                        )
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
                                        .withBodyFile(
                                                "search/success.json"
                                        )
                        )
        );
    }

    public static void stubBlankQueryBadRequest(
            WireMockServer wireMockServer
    ) {
        System.out.println("Stubbing blank query bad request response for /search endpoint");

        wireMockServer.stubFor(
                get(urlPathEqualTo("/search"))
                        .withHeader(
                                "Authorization",
                                equalTo("Bearer valid-token")
                        )
                        .withQueryParam(
                                "q",
                                equalTo("")
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
                                        .withStatus(400)
                                        .withHeader(
                                                "Content-Type",
                                                "application/json"
                                        )
                                        .withBodyFile(
                                                "search/bad-request.json"
                                        )
                        )
        );
    }

    public static void stubMissingAuthentication(
            WireMockServer wireMockServer
    ) {
        System.out.println("Stubbing missing authentication response for /search endpoint");

        wireMockServer.stubFor(
                get(urlPathEqualTo("/search"))
                        .withHeader("Authorization", absent())
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
                                        .withStatus(401)
                                        .withHeader(
                                                "Content-Type",
                                                "application/json"
                                        )
                                        .withBodyFile(
                                                "search/unauthorized.json"
                                        )
                        )
        );
    }

    public static void stubForbidden(
            WireMockServer wireMockServer
    ) {
        System.out.println("Stubbing forbidden response for /search endpoint");

        wireMockServer.stubFor(
                get(urlPathEqualTo("/search"))
                        .withHeader(
                                "Authorization",
                                equalTo("Bearer forbidden-token")
                        )
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
                                        .withStatus(403)
                                        .withHeader(
                                                "Content-Type",
                                                "application/json"
                                        )
                                        .withBodyFile(
                                                "search/forbidden.json"
                                        )
                        )
        );
    }

    public static void stubRateLimited(
            WireMockServer wireMockServer
    ) {
        System.out.println("Stubbing rate limited response for /search endpoint");
        wireMockServer.stubFor(
                get(urlPathEqualTo("/search"))
                        .withHeader(
                                "Authorization",
                                equalTo("Bearer rate-limited-token")
                        )
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
                                        .withStatus(429)
                                        .withHeader(
                                                "Content-Type",
                                                "application/json"
                                        )
                                        .withHeader(
                                                "Retry-After",
                                                "30"
                                        )
                                        .withBodyFile(
                                                "search/rate-limited.json"
                                        )
                        )
        );
    }

    public static void stubServerError(
            WireMockServer wireMockServer
    ) {
        System.out.println("Stubbing server error response for /search endpoint");
        wireMockServer.stubFor(
                get(urlPathEqualTo("/search"))
                        .withHeader(
                                "Authorization",
                                equalTo("Bearer server-error-token")
                        )
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
                                        .withStatus(500)
                                        .withHeader(
                                                "Content-Type",
                                                "application/json"
                                        )
                                        .withBodyFile(
                                                "search/server-error.json"
                                        )
                        )
        );
    }

    public static void stubInvalidSearchContract(
            WireMockServer wireMockServer
    ) {
        System.out.println("Stubbing invalid search contract response for /search endpoint");

        wireMockServer.stubFor(
                get(urlPathEqualTo("/search"))
                        .withHeader(
                                "Authorization",
                                equalTo(
                                        "Bearer invalid-contract-token"
                                )
                        )
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
                                        .withBodyFile(
                                                "search/invalid-contract.json"
                                        )
                        )
        );
    }

    public static void stubMissingQueryBadRequest(
            WireMockServer wireMockServer
    ) {
        System.out.println("Stubbing missing query bad request response for /search endpoint");

        wireMockServer.stubFor(
                get(urlPathEqualTo("/search"))
                        .withHeader(
                                "Authorization",
                                equalTo("Bearer missing-query-token")
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
                                        .withStatus(400)
                                        .withHeader(
                                                "Content-Type",
                                                "application/json"
                                        )
                                        .withBodyFile(
                                                "search/missing-query.json"
                                        )
                        )
        );
    }

    public static void stubInvalidPageBadRequest(
            WireMockServer wireMockServer
    ) {
        System.out.println("Stubbing invalid page bad request response for /search endpoint");
        wireMockServer.stubFor(
                get(urlPathEqualTo("/search"))
                        .withHeader(
                                "Authorization",
                                equalTo("Bearer valid-token")
                        )
                        .withQueryParam(
                                "q",
                                equalTo("machine learning")
                        )
                        .withQueryParam(
                                "page",
                                matching("0|-1")
                        )
                        .withQueryParam(
                                "pageSize",
                                equalTo("2")
                        )
                        .willReturn(
                                aResponse()
                                        .withStatus(400)
                                        .withHeader(
                                                "Content-Type",
                                                "application/json"
                                        )
                                        .withBodyFile(
                                                "search/invalid-page.json"
                                        )
                        )
        );
    }

    public static void stubInvalidPageSizeBadRequest(
            WireMockServer wireMockServer
    ) {
        System.out.println("Stubbing invalid page size bad request response for /search endpoint");
        wireMockServer.stubFor(
                get(urlPathEqualTo("/search"))
                        .withHeader(
                                "Authorization",
                                equalTo("Bearer valid-token")
                        )
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
                                matching("0|101")
                        )
                        .willReturn(
                                aResponse()
                                        .withStatus(400)
                                        .withHeader(
                                                "Content-Type",
                                                "application/json"
                                        )
                                        .withBodyFile(
                                                "search/invalid-page-size.json"
                                        )
                        )
        );
    }


}