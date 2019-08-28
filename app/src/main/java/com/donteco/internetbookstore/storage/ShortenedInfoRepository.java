package com.donteco.internetbookstore.storage;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.donteco.internetbookstore.books.ShortenedBookInfo;
import com.donteco.internetbookstore.dao.ShortenedBooksDao;
import com.donteco.internetbookstore.db.ShortenedBooksDB;

import java.util.List;

public class ShortenedInfoRepository
{
    private ShortenedBooksDao shortenedBooksDao;

    public ShortenedInfoRepository(Application application)
    {
        ShortenedBooksDB shortenedBooksDB = ShortenedBooksDB.getDataBase(application);
        shortenedBooksDao = shortenedBooksDB.booksDao();
    }

    public void insert(List<ShortenedBookInfo> books){
        new Thread(() -> shortenedBooksDao.insertBooks(books)).start();
    }

    public LiveData<List<ShortenedBookInfo>> getBooksByRequest(String request){
        return shortenedBooksDao.getShortenedBooksByRequest("%"+request+"%");
    }


    /*private static class InsertAsyncTask extends AsyncTask<List<ShortenedBookInfo>, Void, Void>
    {
        private ShortenedBooksDao dao;

        private InsertAsyncTask(ShortenedBooksDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(List<ShortenedBookInfo>... lists) {
           dao.insertBooks(lists[0]);
            return null;
        }
    }*/
}
