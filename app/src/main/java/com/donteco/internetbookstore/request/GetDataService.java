package com.donteco.internetbookstore.request;

import com.donteco.internetbookstore.books.FullBookInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetDataService {

    @GET("/1.0/search/{userInput}")
    Call<ServerSearchResponse> getAllBooks(@Path("userInput") String userInput);

    @GET("/1.0/search/{userInput}")
    Call<ServerSearchResponse> getAllBooks(@Path("userInput") String userInput, @Query("page") int pageNum);

    @GET("/1.0/books/{bookID}")
    Call<FullBookInfo> getBookById(@Path("bookID") long bookID);

    @GET("/1.0/new")
    Call<ServerSearchResponceNewBooks> getNewBooks();
}
