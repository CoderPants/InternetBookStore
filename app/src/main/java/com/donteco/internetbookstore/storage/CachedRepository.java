package com.donteco.internetbookstore.storage;

import android.app.Application;

import com.donteco.internetbookstore.books.CachedBook;
import com.donteco.internetbookstore.dao.CachedBookDao;
import com.donteco.internetbookstore.db.CachedBooksDB;

import java.util.List;

public class CachedRepository
{
    private CachedBookDao dao;

    public CachedRepository(Application application)
    {
        CachedBooksDB cachedBooksDB = CachedBooksDB.getInstance(application);
        dao = cachedBooksDB.cachedBookDao();
    }

    public void insert(List<CachedBook> cachedBooks) {
        new Thread(() -> dao.insert(cachedBooks));
    }
}
