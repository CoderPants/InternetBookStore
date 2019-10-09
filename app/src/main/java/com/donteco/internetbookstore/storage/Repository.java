package com.donteco.internetbookstore.storage;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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

    private static Repository repository;

    private Repository(Application application)
    {
        DataBase dataBase = DataBase.getDataBase(application);
        shortenedBooksDao = dataBase.shortenedBooksDao();
        fullInfoBooksDao = dataBase.fullInfoBooksDao();
        cachedBookDao = dataBase.cachedBookDao();
        bookInCartDao = dataBase.bookInCartDao();
    }

    public static Repository getInstance(Application application){
        if(repository == null)
            synchronized (Repository.class)
            {
                if(repository == null)
                    repository = new Repository(application);

            }
        return repository;
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

    public LiveData<List<BookInCart>> getCart(){
        return bookInCartDao.getCart();
    }

    public LiveData<BookInCart> getBookInCartById(long id){
        return bookInCartDao.getBookInCartById(id);
    }

    public LiveData<List<ShortenedBookInfo>> getShortenedBooksInfoByRequest(String request){
        return shortenedBooksDao.getShortenedBooksByRequest("%"+request+"%");
    }

    public LiveData<FullBookInfo> getFullBookInfoById(long id){
        return fullInfoBooksDao.getFullBookInfoById(id);
    }

}
