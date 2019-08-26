package com.donteco.internetbookstore.storage;

import android.app.Application;
import android.os.AsyncTask;

import com.donteco.internetbookstore.books.FullBookInfo;
import com.donteco.internetbookstore.dao.FullBooksInfoDB_Impl;
import com.donteco.internetbookstore.dao.FullInfoBooksDao;

import java.util.List;

public class FullInfoRepository
{
    private FullInfoBooksDao fullInfoDao;
    private List<FullBookInfo> books;

    public FullInfoRepository(Application application)
    {
        //Singleton
        FullBooksInfoDB_Impl fullBooksInfoDB_ = new FullBooksInfoDB_Impl();
        fullInfoDao = fullBooksInfoDB_.fullInfoBooksDao();
        books = fullInfoDao.getFullBooks();
    }

    public List<FullBookInfo> getBooks() {
        return books;
    }

    public void insert(FullBookInfo book) {
        new InsertAsyncTask(fullInfoDao).execute(book);
    }

    private static class InsertAsyncTask extends AsyncTask<FullBookInfo, Void, Void>
    {
        private FullInfoBooksDao dao;

        private InsertAsyncTask(FullInfoBooksDao dao){
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(FullBookInfo... fullBookInfos) {
            dao.insertFullBook(fullBookInfos[0]);
            return null;
        }
    }
}
