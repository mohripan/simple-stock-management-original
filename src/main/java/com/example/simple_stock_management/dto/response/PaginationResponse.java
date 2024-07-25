package com.example.simple_stock_management.dto.response;

import com.example.simple_stock_management.dto.detail.PaginationDetails;

import java.util.List;

public class PaginationResponse<T> {
    private PaginationDetails search;
    private List<T> values;

    public PaginationResponse() {
    }

    public PaginationResponse(PaginationDetails search, List<T> values) {
        this.search = search;
        this.values = values;
    }

    public PaginationDetails getSearch() {
        return search;
    }

    public void setSearch(PaginationDetails search) {
        this.search = search;
    }

    public List<T> getValues() {
        return values;
    }

    public void setValues(List<T> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "PaginationResponse{" +
                "search=" + search +
                ", values=" + values +
                '}';
    }
}
