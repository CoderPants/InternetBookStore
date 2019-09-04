package com.donteco.internetbookstore.helper;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.donteco.internetbookstore.books.CachedBook;
import com.donteco.internetbookstore.books.ShortenedBookInfo;
import com.donteco.internetbookstore.constants.ConstantsForApp;

import java.util.ArrayList;
import java.util.List;

public class SearchBooksHelper
{
    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean noListInDB(List<ShortenedBookInfo> responseFromServer,
                              List<ShortenedBookInfo> listFromAdapter,
                              int lastPackOfBooks)
    {
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

    public static List<CachedBook> convertToCachedBooks(List<ShortenedBookInfo> receivedShortenedBooksInfo,
                                                    String userInput)
    {
        List<CachedBook> result = new ArrayList<>();

        for (ShortenedBookInfo shortenedBookInfo : receivedShortenedBooksInfo)
            result.add(new CachedBook(userInput, shortenedBookInfo.getId()));

        return result;
    }
}
