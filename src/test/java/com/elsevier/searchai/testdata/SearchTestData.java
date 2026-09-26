package com.elsevier.searchai.testdata;

import com.elsevier.searchai.models.SearchRequest;
import com.elsevier.searchai.models.SearchRequestBuilder;

public final class SearchTestData {

    public static final String DEFAULT_QUERY =
            "machine learning";

    public static final int DEFAULT_PAGE =
            1;

    public static final int DEFAULT_PAGE_SIZE =
            2;

    private SearchTestData() {
    }

    public static SearchRequestBuilder validSearch() {

        return SearchRequest.builder()
                .query(DEFAULT_QUERY)
                .page(DEFAULT_PAGE)
                .pageSize(DEFAULT_PAGE_SIZE);
    }
    public static SearchRequestBuilder filteredSearch() {

        return validSearch()
                .sort("publicationDate")
                .sortDirection("DESC")
                .publicationYearFrom(2020)
                .publicationYearTo(2026)
                .subject("computer-science")
                .documentType("article");
    }

    public static SearchRequestBuilder secondPageSearch() {

        return validSearch()
                .page(2);
    }
}