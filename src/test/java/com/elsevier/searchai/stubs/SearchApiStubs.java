package com.elsevier.searchai.stubs;

import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public final class SearchApiStubs {

    private SearchApiStubs() {
    }

    public static void stubSuccessfulSearch(
            WireMockServer wireMockServer
    ) {
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
                                matching("1|2|50|100")
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
    ) {wireMockServer.stubFor(
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

    public static void stubInvalidPageFormat(
            WireMockServer wireMockServer
    ) {

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
                                equalTo("abc")
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
                                                "search/invalid-page-format.json"
                                        )
                        )
        );
    }

    public static void stubInvalidPageSizeFormat(
            WireMockServer wireMockServer
    ) {

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
                                equalTo("abc")
                        )
                        .willReturn(
                                aResponse()
                                        .withStatus(400)
                                        .withHeader(
                                                "Content-Type",
                                                "application/json"
                                        )
                                        .withBodyFile(
                                                "search/invalid-page-size-format.json"
                                        )
                        )
        );
    }

    public static void stubEmptyPage(
            WireMockServer wireMockServer
    ) {

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
                                equalTo("")
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
                                                "search/empty-page.json"
                                        )
                        )
        );
    }

    public static void stubEmptyPageSize(
            WireMockServer wireMockServer
    ) {

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
                                equalTo("")
                        )
                        .willReturn(
                                aResponse()
                                        .withStatus(400)
                                        .withHeader(
                                                "Content-Type",
                                                "application/json"
                                        )
                                        .withBodyFile(
                                                "search/empty-page-size.json"
                                        )
                        )
        );
    }
}