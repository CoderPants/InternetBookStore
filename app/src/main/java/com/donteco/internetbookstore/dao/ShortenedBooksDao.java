package com.donteco.internetbookstore.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.donteco.internetbookstore.books.ShortenedBookInfo;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ShortenedBooksDao
{
    @Insert(onConflict = REPLACE)
    void insertShortenedBook(ShortenedBookInfo entity);

    @Insert(onConflict = REPLACE)
    void insertBooks(List<ShortenedBookInfo> books);

    @Update
    void updateShortenedBook(ShortenedBookInfo entity);

    @Delete
    void deleteShortenedBook(ShortenedBookInfo entity);

    @Query("SELECT * FROM shortened_book_description WHERE title LIKE :userRequest")
    List<ShortenedBookInfo> getShortenedBooksByRequest(String userRequest);

    @Query("SELECT * FROM shortened_book_description")
    List<ShortenedBookInfo> getShortenedBooks();
}
