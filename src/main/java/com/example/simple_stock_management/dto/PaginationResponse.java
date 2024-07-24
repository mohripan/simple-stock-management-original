package com.example.simple_stock_management.dto;

import java.util.List;

public class PaginationResponse<T> {
    private long total;
    private int totalPage;
    private int sizePerPage;
    private int pageAt;
    private List<T> values;

    public PaginationResponse() {
    }

    public PaginationResponse(long total, int totalPage, int sizePerPage, int pageAt, List<T> values) {
        this.total = total;
        this.totalPage = totalPage;
        this.sizePerPage = sizePerPage;
        this.pageAt = pageAt;
        this.values = values;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getSizePerPage() {
        return sizePerPage;
    }

    public void setSizePerPage(int sizePerPage) {
        this.sizePerPage = sizePerPage;
    }

    public int getPageAt() {
        return pageAt;
    }

    public void setPageAt(int pageAt) {
        this.pageAt = pageAt;
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
                "total=" + total +
                ", totalPage=" + totalPage +
                ", sizePerPage=" + sizePerPage +
                ", pageAt=" + pageAt +
                ", values=" + values +
                '}';
    }
}
