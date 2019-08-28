package com.donteco.internetbookstore.db;

import android.app.Application;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.donteco.internetbookstore.books.CachedBook;
import com.donteco.internetbookstore.dao.CachedBookDao;
import com.donteco.internetbookstore.help.ConstantsForApp;

@Database(entities = {CachedBook.class}, version = 1)
public abstract class CachedBooksDB extends RoomDatabase
{
    public abstract CachedBookDao cachedBookDao();

    private static CachedBooksDB INSTANCE;

    public synchronized static CachedBooksDB getInstance(Context context)
    {
        if(INSTANCE == null)
        {
            INSTANCE = Room.databaseBuilder(context,
                    CachedBooksDB.class,
                    ConstantsForApp.CACHED_BOOK_DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
