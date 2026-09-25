package com.elsevier.searchai.filters;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

import java.util.UUID;

public class CorrelationIdFilter implements Filter {

    public static final String CORRELATION_ID_HEADER =
            "X-Correlation-ID";

    @Override
    public Response filter(
            FilterableRequestSpecification requestSpec,
            FilterableResponseSpecification responseSpec,
            FilterContext context
    ) {

        String correlationId = UUID.randomUUID().toString();

        requestSpec.header(
                CORRELATION_ID_HEADER,
                correlationId
        );

        return context.next(
                requestSpec,
                responseSpec
        );
    }
}