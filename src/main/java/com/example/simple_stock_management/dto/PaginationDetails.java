package com.example.simple_stock_management.dto;

public class PaginationDetails {
    private long total;
    private int totalPage;
    private int sizePerPage;
    private int pageAt;

    public PaginationDetails() {
    }

    public PaginationDetails(long total, int totalPage, int sizePerPage, int pageAt) {
        this.total = total;
        this.totalPage = totalPage;
        this.sizePerPage = sizePerPage;
        this.pageAt = pageAt;
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

    @Override
    public String toString() {
        return "PaginationDetails{" +
                "total=" + total +
                ", totalPage=" + totalPage +
                ", sizePerPage=" + sizePerPage +
                ", pageAt=" + pageAt +
                '}';
    }
}
