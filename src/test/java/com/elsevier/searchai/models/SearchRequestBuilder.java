package com.elsevier.searchai.models;

public final class SearchRequestBuilder {

    private String query;
    private int page;
    private int pageSize;
    private String sort;
    private String sortDirection;
    private Integer publicationYearFrom;
    private Integer publicationYearTo;
    private String subject;
    private String documentType;

    public SearchRequestBuilder query(String query) {
        this.query = query;
        return this;
    }

    public SearchRequestBuilder page(int page) {
        this.page = page;
        return this;
    }

    public SearchRequestBuilder pageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public SearchRequestBuilder sort(String sort) {
        this.sort = sort;
        return this;
    }

    public SearchRequestBuilder sortDirection(
            String sortDirection
    ) {
        this.sortDirection = sortDirection;
        return this;
    }

    public SearchRequestBuilder publicationYearFrom(
            Integer publicationYearFrom
    ) {
        this.publicationYearFrom = publicationYearFrom;
        return this;
    }

    public SearchRequestBuilder publicationYearTo(
            Integer publicationYearTo
    ) {
        this.publicationYearTo = publicationYearTo;
        return this;
    }

    public SearchRequestBuilder subject(String subject) {
        this.subject = subject;
        return this;
    }

    public SearchRequestBuilder documentType(
            String documentType
    ) {
        this.documentType = documentType;
        return this;
    }

    public SearchRequest build() {
        return new SearchRequest(
                query,
                page,
                pageSize,
                sort,
                sortDirection,
                publicationYearFrom,
                publicationYearTo,
                subject,
                documentType
        );
    }
}