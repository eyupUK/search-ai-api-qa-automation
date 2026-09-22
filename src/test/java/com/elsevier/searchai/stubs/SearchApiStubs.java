package com.elsevier.searchai.stubs;

import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
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
}