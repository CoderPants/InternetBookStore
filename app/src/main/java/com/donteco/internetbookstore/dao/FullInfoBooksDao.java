package com.donteco.internetbookstore.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.donteco.internetbookstore.books.FullBookInfo;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface FullInfoBooksDao
{
    @Insert(onConflict = REPLACE)
    void insertFullBook(FullBookInfo entity);

    @Update
    void updateFullBook(FullBookInfo entity);

    @Delete
    void deleteFullBook(FullBookInfo entity);

    @Query("SELECT * FROM full_books_description WHERE id LIKE :id")
    FullBookInfo getFullBook(int id);

    @Query("SELECT * FROM full_books_description")
    List<FullBookInfo> getFullBooks();
}
