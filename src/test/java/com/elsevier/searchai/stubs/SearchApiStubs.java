package com.elsevier.searchai.stubs;

import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public final class SearchApiStubs {

    private SearchApiStubs() {
    }

    public static void stubSearchResponse(
            WireMockServer wireMockServer,
            String accessToken,
            String query,
            String page,
            String pageSize,
            int statusCode,
            String bodyFile
    ) {

        wireMockServer.stubFor(
                get(urlPathEqualTo("/search"))
                        .withHeader(
                                "Authorization",
                                equalTo("Bearer " + accessToken)
                        )
                        .withQueryParam(
                                "q",
                                equalTo(query)
                        )
                        .withQueryParam(
                                "page",
                                equalTo(page)
                        )
                        .withQueryParam(
                                "pageSize",
                                equalTo(pageSize)
                        )
                        .willReturn(
                                aResponse()
                                        .withStatus(statusCode)
                                        .withHeader(
                                                "Content-Type",
                                                "application/json"
                                        )
                                        .withBodyFile(bodyFile)
                        )
        );
    }

    public static void stubSearchWithoutAuthentication(
            WireMockServer wireMockServer,
            String query,
            String page,
            String pageSize,
            int statusCode,
            String bodyFile
    ) {

        wireMockServer.stubFor(
                get(urlPathEqualTo("/search"))
                        .withHeader("Authorization", absent())
                        .withQueryParam(
                                "q",
                                equalTo(query)
                        )
                        .withQueryParam(
                                "page",
                                equalTo(page)
                        )
                        .withQueryParam(
                                "pageSize",
                                equalTo(pageSize)
                        )
                        .willReturn(
                                aResponse()
                                        .withStatus(statusCode)
                                        .withHeader(
                                                "Content-Type",
                                                "application/json"
                                        )
                                        .withBodyFile(bodyFile)
                        )
        );
    }

    public static void stubSearchWithoutQuery(
            WireMockServer wireMockServer,
            String accessToken,
            String page,
            String pageSize,
            int statusCode,
            String bodyFile
    ) {

        wireMockServer.stubFor(
                get(urlPathEqualTo("/search"))
                        .withHeader(
                                "Authorization",
                                equalTo("Bearer " + accessToken)
                        )
                        .withQueryParam("q", absent())
                        .withQueryParam(
                                "page",
                                equalTo(page)
                        )
                        .withQueryParam(
                                "pageSize",
                                equalTo(pageSize)
                        )
                        .willReturn(
                                aResponse()
                                        .withStatus(statusCode)
                                        .withHeader(
                                                "Content-Type",
                                                "application/json"
                                        )
                                        .withBodyFile(bodyFile)
                        )
        );
    }

    public static void stubRateLimitedSearch(
            WireMockServer wireMockServer,
            String accessToken,
            String query,
            String page,
            String pageSize,
            int statusCode,
            String bodyFile,
            String retryAfter
    ) {

        wireMockServer.stubFor(
                get(urlPathEqualTo("/search"))
                        .withHeader(
                                "Authorization",
                                equalTo("Bearer " + accessToken)
                        )
                        .withQueryParam(
                                "q",
                                equalTo(query)
                        )
                        .withQueryParam(
                                "page",
                                equalTo(page)
                        )
                        .withQueryParam(
                                "pageSize",
                                equalTo(pageSize)
                        )
                        .willReturn(
                                aResponse()
                                        .withStatus(statusCode)
                                        .withHeader(
                                                "Content-Type",
                                                "application/json"
                                        )
                                        .withHeader(
                                                "Retry-After",
                                                retryAfter
                                        )
                                        .withBodyFile(bodyFile)
                        )
        );
    }
    public static void stubFilteredSearch(
            WireMockServer wireMockServer,
            String accessToken,
            String query,
            String page,
            String pageSize,
            String sort,
            String sortDirection,
            String publicationYearFrom,
            String publicationYearTo,
            String subject,
            String documentType,
            int statusCode,
            String bodyFile
    ) {

        wireMockServer.stubFor(
                get(urlPathEqualTo("/search"))
                        .withHeader(
                                "Authorization",
                                equalTo(
                                        "Bearer " + accessToken
                                )
                        )
                        .withQueryParam(
                                "q",
                                equalTo(query)
                        )
                        .withQueryParam(
                                "page",
                                equalTo(page)
                        )
                        .withQueryParam(
                                "pageSize",
                                equalTo(pageSize)
                        )
                        .withQueryParam(
                                "sort",
                                equalTo(sort)
                        )
                        .withQueryParam(
                                "sortDirection",
                                equalTo(sortDirection)
                        )
                        .withQueryParam(
                                "publicationYearFrom",
                                equalTo(publicationYearFrom)
                        )
                        .withQueryParam(
                                "publicationYearTo",
                                equalTo(publicationYearTo)
                        )
                        .withQueryParam(
                                "subject",
                                equalTo(subject)
                        )
                        .withQueryParam(
                                "documentType",
                                equalTo(documentType)
                        )
                        .willReturn(
                                aResponse()
                                        .withStatus(statusCode)
                                        .withHeader(
                                                "Content-Type",
                                                "application/json"
                                        )
                                        .withBodyFile(bodyFile)
                        )
        );
    }
}