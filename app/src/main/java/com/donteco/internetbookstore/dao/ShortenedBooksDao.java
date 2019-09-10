package com.donteco.internetbookstore.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.donteco.internetbookstore.books.ShortenedBookInfo;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ShortenedBooksDao {

    @Insert(onConflict = REPLACE)
    void insertBooks(List<ShortenedBookInfo> books);


    //@Query("SELECT * FROM shortened_book_description WHERE title LIKE :userRequest")
    @Query("SELECT shortened_book_description.* " +
            "FROM shortened_book_description" +
            " JOIN cached_books ON cached_books.id = shortened_book_description.id " +
            "WHERE cached_books.userRequest LIKE :userRequest ")
    LiveData<List<ShortenedBookInfo>> getShortenedBooksByRequest(String userRequest);

}
