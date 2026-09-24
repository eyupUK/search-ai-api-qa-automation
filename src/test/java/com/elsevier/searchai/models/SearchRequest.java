package com.elsevier.searchai.models;

public final class SearchRequest {

    private final String query;
    private final int page;
    private final int pageSize;

    SearchRequest(
            String query,
            int page,
            int pageSize
    ) {
        this.query = query;
        this.page = page;
        this.pageSize = pageSize;
    }

    public String getQuery() {
        return query;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public static SearchRequestBuilder builder() {
        return new SearchRequestBuilder();
    }
}