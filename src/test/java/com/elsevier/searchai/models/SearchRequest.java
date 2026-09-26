package com.elsevier.searchai.models;

public final class SearchRequest {

    private final String query;
    private final int page;
    private final int pageSize;
    private final String sort;
    private final String sortDirection;
    private final Integer publicationYearFrom;
    private final Integer publicationYearTo;
    private final String subject;
    private final String documentType;

    SearchRequest(
            String query,
            int page,
            int pageSize,
            String sort,
            String sortDirection,
            Integer publicationYearFrom,
            Integer publicationYearTo,
            String subject,
            String documentType
    ) {
        this.query = query;
        this.page = page;
        this.pageSize = pageSize;
        this.sort = sort;
        this.sortDirection = sortDirection;
        this.publicationYearFrom = publicationYearFrom;
        this.publicationYearTo = publicationYearTo;
        this.subject = subject;
        this.documentType = documentType;
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

    public String getSort() {
        return sort;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public Integer getPublicationYearFrom() {
        return publicationYearFrom;
    }

    public Integer getPublicationYearTo() {
        return publicationYearTo;
    }

    public String getSubject() {
        return subject;
    }

    public String getDocumentType() {
        return documentType;
    }

    public static SearchRequestBuilder builder() {
        return new SearchRequestBuilder();
    }
}