package com.donteco.internetbookstore.request;

import com.donteco.internetbookstore.books.ShortenedBookInfo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServerSearchResponceNewBooks
{
    private int error;
    private int total;

    @SerializedName("books")
    private List<ShortenedBookInfo> shortenedBooksInfo;

    public ServerSearchResponceNewBooks(int error, int total, List<ShortenedBookInfo> shortenedBooksInfo) {
        this.error = error;
        this.total = total;
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

    public List<ShortenedBookInfo> getShortenedBooksInfo() {
        return shortenedBooksInfo;
    }

    public void setShortenedBooksInfo(List<ShortenedBookInfo> shortenedBooksInfo) {
        this.shortenedBooksInfo = shortenedBooksInfo;
    }

    @Override
    public String toString() {
        return "ServerSearchResponceNewBooks{" +
                "error=" + error +
                ", total=" + total +
                ", shortenedBooksInfo=" + shortenedBooksInfo +
                '}';
    }
}
