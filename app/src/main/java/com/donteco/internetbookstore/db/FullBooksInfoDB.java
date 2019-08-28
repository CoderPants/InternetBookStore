package com.donteco.internetbookstore.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.donteco.internetbookstore.books.FullBookInfo;
import com.donteco.internetbookstore.converters.FullBookInfoTypeConverter;
import com.donteco.internetbookstore.dao.FullInfoBooksDao;
import com.donteco.internetbookstore.help.ConstantsForApp;

@Database(entities = {FullBookInfo.class}, version = 1)

@TypeConverters({FullBookInfoTypeConverter.class})
public abstract class FullBooksInfoDB extends RoomDatabase {
    public abstract FullInfoBooksDao fullInfoBooksDao();

    private static FullBooksInfoDB INSTANCE;

    public static synchronized FullBooksInfoDB getDataBase(Context context)
    {
        if(INSTANCE == null)
        {
            INSTANCE = Room.databaseBuilder(context,
                    FullBooksInfoDB.class,
                    ConstantsForApp.FULL_BOOK_INFO_DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();

        }
        return INSTANCE;
    }
}
