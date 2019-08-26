package com.donteco.internetbookstore.storage;

import android.app.Application;
import android.os.AsyncTask;

import com.donteco.internetbookstore.books.ShortenedBookInfo;
import com.donteco.internetbookstore.dao.ShortenedBooksDB_Impl;
import com.donteco.internetbookstore.dao.ShortenedBooksDao;

import java.util.List;

public class ShortenedInfoRepository
{
    private ShortenedBooksDao shortenedBooksDao;
    private List<ShortenedBookInfo> books;

    public ShortenedInfoRepository(Application application)
    {
        ShortenedBooksDB_Impl shortenedBooksDB_ = new ShortenedBooksDB_Impl();
        shortenedBooksDao = shortenedBooksDB_.booksDao();
    }

    public void insert(List<ShortenedBookInfo> books){
        new InsertAsyncTask(shortenedBooksDao).execute(books);
    }

    private static class InsertAsyncTask extends AsyncTask<List<ShortenedBookInfo>, Void, Void>
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
    }
}
