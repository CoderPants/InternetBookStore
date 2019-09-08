package com.donteco.internetbookstore.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.donteco.internetbookstore.books.BookInCart;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface BookInCartDao
{
    @Insert(onConflict = REPLACE)
    void insert(BookInCart bookInCart);

    @Update
    void update(BookInCart bookInCart);

    //By primary key
    @Delete
    void delete(BookInCart bookInCart);

    @Query("SELECT * " +
            "FROM shopping_cart")
    LiveData<List<BookInCart>> getCart();

    @Query("SELECT *" +
            "FROM shopping_cart " +
            "WHERE id LIKE :id")
    BookInCart getBookInCartById(long id);

}
