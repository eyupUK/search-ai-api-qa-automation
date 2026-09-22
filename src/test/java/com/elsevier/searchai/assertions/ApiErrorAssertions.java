package com.elsevier.searchai.assertions;

import com.elsevier.searchai.models.ErrorResponse;
import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public final class ApiErrorAssertions {

    private ApiErrorAssertions() {
    }

    public static void assertError(
            Response response,
            int expectedStatusCode,
            String expectedErrorCode,
            String expectedMessage
    ) {

        response.then()
                .statusCode(expectedStatusCode)
                .contentType("application/json");

        ErrorResponse errorResponse =
                response.as(ErrorResponse.class);

        assertNotNull(errorResponse);

        assertEquals(
                expectedErrorCode,
                errorResponse.getError()
        );

        assertEquals(
                expectedMessage,
                errorResponse.getMessage()
        );
    }
}