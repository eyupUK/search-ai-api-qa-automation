package com.elsevier.searchai.auth;

public final class StaticTokenProvider implements TokenProvider {

    private final String accessToken;

    public StaticTokenProvider(String accessToken) {

        if (accessToken == null || accessToken.isBlank()) {
            throw new IllegalArgumentException(
                    "Access token must not be null or blank"
            );
        }

        this.accessToken = accessToken;
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }
}