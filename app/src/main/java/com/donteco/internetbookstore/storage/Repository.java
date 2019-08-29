package com.donteco.internetbookstore.storage;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.donteco.internetbookstore.books.CachedBook;
import com.donteco.internetbookstore.books.FullBookInfo;
import com.donteco.internetbookstore.books.ShortenedBookInfo;
import com.donteco.internetbookstore.dao.CachedBookDao;
import com.donteco.internetbookstore.dao.FullInfoBooksDao;
import com.donteco.internetbookstore.dao.ShortenedBooksDao;
import com.donteco.internetbookstore.db.DataBase;
import com.donteco.internetbookstore.help.ConstantsForApp;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class Repository
{
    private ShortenedBooksDao shortenedBooksDao;
    private FullInfoBooksDao fullInfoBooksDao;
    private CachedBookDao cachedBookDao;

    public Repository(Application application)
    {
        DataBase dataBase = DataBase.getDataBase(application);
        shortenedBooksDao = dataBase.shortenedBooksDao();
        fullInfoBooksDao = dataBase.fullInfoBooksDao();
        cachedBookDao = dataBase.cachedBookDao();
    }

    public void insertShortenedBooksInfo(List<ShortenedBookInfo> books){
        new Thread(() -> shortenedBooksDao.insertBooks(books)).start();
    }

    public void insertFullBookInfo(FullBookInfo book) {
        new Thread(() -> fullInfoBooksDao.insertFullBook(book));
    }

    public void insertCachedBooksInfo(List<CachedBook> cachedBooks) {
        new Thread(() -> cachedBookDao.insert(cachedBooks));
    }

    public LiveData<List<ShortenedBookInfo>> getShortenedBooksInfoByRequest(String request){
        return shortenedBooksDao.getShortenedBooksByRequest("%"+request+"%");
    }

    public FullBookInfo getFullBookInfo(int id)
    {
        GetFullInfoAsyncTask asyncTask = new GetFullInfoAsyncTask(fullInfoBooksDao);
        asyncTask.execute(id);
        try {
            return asyncTask.get();
        } catch (ExecutionException | InterruptedException e) {
            Log.e(ConstantsForApp.LOG_TAG, "Caught exception in getFullBoonInfo " + e);
        }
        return null;
    }

    private static class GetFullInfoAsyncTask extends AsyncTask<Integer, Void, FullBookInfo>
    {
        private FullInfoBooksDao dao;

        private GetFullInfoAsyncTask(FullInfoBooksDao dao){
            this.dao = dao;
        }

        @Override
        protected FullBookInfo doInBackground(Integer... integers) {
            return dao.getFullBookByRequest(integers[0]);
        }

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
