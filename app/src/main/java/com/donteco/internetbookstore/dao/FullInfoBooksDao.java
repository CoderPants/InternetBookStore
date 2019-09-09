package com.donteco.internetbookstore.dao;

import androidx.lifecycle.LiveData;
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

    @Query("SELECT * FROM full_books_description WHERE id LIKE :id")
    FullBookInfo getFullBookByRequest(long id);

    @Query("SELECT * FROM full_books_description WHERE id LIKE :id")
    LiveData<FullBookInfo> getFullBookInfoById(long id);

    @Query("SELECT * FROM full_books_description")
    LiveData< List<FullBookInfo> > getFullBooks();
}
