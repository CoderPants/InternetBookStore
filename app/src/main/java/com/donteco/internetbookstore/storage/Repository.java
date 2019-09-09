package com.donteco.internetbookstore.storage;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.donteco.internetbookstore.books.BookInCart;
import com.donteco.internetbookstore.books.CachedBook;
import com.donteco.internetbookstore.books.FullBookInfo;
import com.donteco.internetbookstore.books.ShortenedBookInfo;
import com.donteco.internetbookstore.dao.BookInCartDao;
import com.donteco.internetbookstore.dao.CachedBookDao;
import com.donteco.internetbookstore.dao.FullInfoBooksDao;
import com.donteco.internetbookstore.dao.ShortenedBooksDao;
import com.donteco.internetbookstore.db.DataBase;
import com.donteco.internetbookstore.constants.ConstantsForApp;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class Repository
{
    private ShortenedBooksDao shortenedBooksDao;
    private FullInfoBooksDao fullInfoBooksDao;
    private CachedBookDao cachedBookDao;
    private BookInCartDao bookInCartDao;

    public Repository(Application application)
    {
        DataBase dataBase = DataBase.getDataBase(application);
        shortenedBooksDao = dataBase.shortenedBooksDao();
        fullInfoBooksDao = dataBase.fullInfoBooksDao();
        cachedBookDao = dataBase.cachedBookDao();
        bookInCartDao = dataBase.bookInCartDao();
    }

    public void insertShortenedBooksInfo(List<ShortenedBookInfo> books){
        new Thread(() -> shortenedBooksDao.insertBooks(books)).start();
    }

    public void insertFullBookInfo(FullBookInfo book) {
        new Thread(() -> fullInfoBooksDao.insertFullBook(book)).start();
    }

    public void insertCachedBooksInfo(List<CachedBook> cachedBooks) {
        new Thread(() -> cachedBookDao.insert(cachedBooks)).start();
    }

    public void insertBookInCartInfo(BookInCart bookInCart){
        new Thread(() -> bookInCartDao.insert(bookInCart)).start();
    }

    public void updateBookInCartInfo(BookInCart bookInCart){
        new Thread(() -> bookInCartDao.update(bookInCart)).start();
    }

    public void deleteBookInCart(BookInCart bookInCart){
        new Thread(() -> bookInCartDao.delete(bookInCart)).start();
    }

    /*public BookInCart getBookInCartById(long id){
        GetBookInCartAsyncTask asyncTask = new GetBookInCartAsyncTask(bookInCartDao);
        asyncTask.execute(id);

        try {
            return asyncTask.get();
        } catch (ExecutionException | InterruptedException e) {
            Log.e(ConstantsForApp.LOG_TAG,
                    "Caught exception in getBookInCartById. Caused by getting info from db. Exception " + e);
        }
        return null;
    }*/

    public LiveData<List<BookInCart>> getCart(){
        return bookInCartDao.getCart();
    }

    public LiveData<BookInCart> getBookInCartById(long id){
        return bookInCartDao.getBookInCartById(id);
    }

    public LiveData<List<ShortenedBookInfo>> getShortenedBooksInfoByRequest(String request){
        return shortenedBooksDao.getShortenedBooksByRequest("%"+request+"%");
    }

    public FullBookInfo getFullBookInfo(long id)
    {
        GetFullInfoAsyncTask asyncTask = new GetFullInfoAsyncTask(fullInfoBooksDao);
        asyncTask.execute(id);
        try {
            return asyncTask.get();
        } catch (ExecutionException | InterruptedException e) {
            Log.e(ConstantsForApp.LOG_TAG,
                    "Caught exception in getFullBoonInfo. Caused by getting info from db. Exception " + e);
        }
        return null;
    }

    private static class GetFullInfoAsyncTask extends AsyncTask<Long, Void, FullBookInfo>
    {
        private FullInfoBooksDao dao;

        private GetFullInfoAsyncTask(FullInfoBooksDao dao){
            this.dao = dao;
        }

        @Override
        protected FullBookInfo doInBackground(Long... longs) {
            return dao.getFullBookByRequest(longs[0]);
        }
    }


    private static class GetBookInCartAsyncTask extends AsyncTask<Long, Void, BookInCart>
    {
        private BookInCartDao dao;

        public GetBookInCartAsyncTask(BookInCartDao dao) {
            this.dao = dao;
        }

        @Override
        protected BookInCart doInBackground(Long... longs) {
            System.out.println("Book id db " + longs[0]);
            return null;//dao.getBookInCartById(longs[0]);
        }
    }
}
