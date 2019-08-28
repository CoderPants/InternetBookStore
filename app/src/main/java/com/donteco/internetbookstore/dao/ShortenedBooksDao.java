package com.donteco.internetbookstore.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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

//@Query("SELECT * FROM shortened_book_description WHERE title LIKE :userRequest")
    @Query("SELECT * " +
            "FROM shortened_book_description" +
            " JOIN cached_books ON id " +
            "WHERE userRequest LIKE :userRequest ")
    LiveData< List<ShortenedBookInfo> > getShortenedBooksByRequest(String userRequest);

    @Query("SELECT * FROM shortened_book_description")
    LiveData< List<ShortenedBookInfo> > getShortenedBooks();

    @Query("DELETE FROM shortened_book_description")
    void deleteAll();
}
