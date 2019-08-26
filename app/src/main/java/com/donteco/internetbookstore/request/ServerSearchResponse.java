package com.donteco.internetbookstore.request;

import com.donteco.internetbookstore.books.ShortenedBookInfo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServerSearchResponse {

    private int error;
    private int total;
    private int page;

    @SerializedName("books")
    private List<ShortenedBookInfo> shortenedBooksInfo;

    public ServerSearchResponse(int error, int total, int page, List<ShortenedBookInfo> shortenedBooksInfo) {
        this.error = error;
        this.total = total;
        this.page = page;
        this.shortenedBooksInfo = shortenedBooksInfo;
    }


    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<ShortenedBookInfo> getShortenedBooksInfo() {
        return shortenedBooksInfo;
    }

    public void setShortenedBooksInfo(List<ShortenedBookInfo> shortenedBooksInfo) {
        this.shortenedBooksInfo = shortenedBooksInfo;
    }
}
