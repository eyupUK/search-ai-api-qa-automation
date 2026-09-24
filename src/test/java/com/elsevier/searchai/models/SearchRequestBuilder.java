package com.elsevier.searchai.models;

public final class SearchRequestBuilder {

    private String query;
    private int page;
    private int pageSize;

    public SearchRequestBuilder query(String query){
        this.query = query;
        return this;
    }

    public SearchRequestBuilder page(int page){
        this.page = page;
        return this;
    }

    public SearchRequestBuilder pageSize(int pageSize){
        this.pageSize = pageSize;
        return this;
    }

    public SearchRequest build(){
        return new SearchRequest(query, page, pageSize);
    }
}