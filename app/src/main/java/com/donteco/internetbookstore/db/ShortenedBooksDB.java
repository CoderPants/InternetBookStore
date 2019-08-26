package com.donteco.internetbookstore.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.donteco.internetbookstore.books.ShortenedBookInfo;
import com.donteco.internetbookstore.dao.ShortenedBooksDao;
import com.donteco.internetbookstore.help.ConstantsForApp;

@Database(entities = {ShortenedBookInfo.class}, version = 1)

public abstract class ShortenedBooksDB extends RoomDatabase {
    public abstract ShortenedBooksDao booksDao();

    private static volatile ShortenedBooksDB INSTANCE;

    public static ShortenedBooksDB getDataBase(Context context){
        if(INSTANCE == null)
        {
            synchronized (ShortenedBooksDB.class)
            {
                if(INSTANCE == null)
                {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ShortenedBooksDB.class, ConstantsForApp.SHORTENED_BOOK_INFO_DB_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
