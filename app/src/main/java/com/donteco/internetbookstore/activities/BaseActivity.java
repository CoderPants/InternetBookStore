package com.donteco.internetbookstore.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.donteco.internetbookstore.books.CachedBook;
import com.donteco.internetbookstore.books.ShortenedBookInfo;
import com.donteco.internetbookstore.constants.ConstantsForApp;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity extends AppCompatActivity {

    protected void getRidOfTopBar() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    protected boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    protected boolean noListInDB(List<ShortenedBookInfo> responseFromServer,
                               List<ShortenedBookInfo> listFromAdapter,
                               int lastPackOfBooks)
    {
        //List<ShortenedBookInfo> listFromAdapter = chooseBooksAdapter.getBooks();

        //Cos we, mostly, won't get numbers, that'll divide by 10 without leaving the remainder
        //int lastPackOfBooks = requestSender.getTotalNumberOfBooks() % 10;

        //For equals book counting
        int counter = 0;

        if(listFromAdapter.isEmpty())
            return true;

        for (ShortenedBookInfo bookInfo : responseFromServer)
            if(listFromAdapter.contains(bookInfo))
                counter++;


        return (counter != ConstantsForApp.AMOUNT_OF_BOOKS_PER_PAGE)
                && (counter != lastPackOfBooks);
    }

    protected List<CachedBook> convertToCachedBooks(List<ShortenedBookInfo> receivedShortenedBooksInfo,
                                                  String userInput)
    {
        List<CachedBook> result = new ArrayList<>();

        for (ShortenedBookInfo shortenedBookInfo : receivedShortenedBooksInfo)
            result.add(new CachedBook(userInput, shortenedBookInfo.getId()));

        return result;
    }
}
