package com.donteco.internetbookstore.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.donteco.internetbookstore.books.CachedBook;
import com.donteco.internetbookstore.books.FullBookInfo;
import com.donteco.internetbookstore.books.ShortenedBookInfo;
import com.donteco.internetbookstore.converters.FullBookInfoTypeConverter;
import com.donteco.internetbookstore.dao.CachedBookDao;
import com.donteco.internetbookstore.dao.FullInfoBooksDao;
import com.donteco.internetbookstore.dao.ShortenedBooksDao;
import com.donteco.internetbookstore.constants.ConstantsForApp;

@Database(entities = {ShortenedBookInfo.class, CachedBook.class, FullBookInfo.class}, version = 1)

@TypeConverters({FullBookInfoTypeConverter.class})
public abstract class DataBase extends RoomDatabase {
    public abstract ShortenedBooksDao shortenedBooksDao();
    public abstract FullInfoBooksDao fullInfoBooksDao();
    public abstract CachedBookDao cachedBookDao();

    private static volatile DataBase INSTANCE;

    public static synchronized DataBase getDataBase(Context context)
    {
        if(INSTANCE == null)
        {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    DataBase.class, ConstantsForApp.SHORTENED_BOOK_INFO_DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return INSTANCE;
    }
}
