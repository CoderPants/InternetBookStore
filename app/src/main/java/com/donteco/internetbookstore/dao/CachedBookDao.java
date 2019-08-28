package com.donteco.internetbookstore.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.donteco.internetbookstore.books.CachedBook;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface CachedBookDao
{
    @Insert (onConflict = REPLACE)
    void insert(List<CachedBook> cachedBooks);

    @Insert (onConflict = REPLACE)
    void insert(CachedBook cachedBook);

    @Query("DELETE FROM cached_books")
    void deleteAll();

    /*@Query("SELECT * FROM SHORTENED_BOOK_DESCRIPTION" +
            " JOIN CACHED_BOOKS ON ID " +
            "WHERE userRequest LIKE :userRequest ")
    List<ShortenedBookInfo> getBooksByRequest(String userRequest);*/
}
