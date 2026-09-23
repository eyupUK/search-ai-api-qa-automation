package com.elsevier.searchai.stubs;

import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.absent;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

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
    ) {

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
}