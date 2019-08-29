package com.donteco.internetbookstore.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.donteco.internetbookstore.books.ShortenedBookInfo;
import com.donteco.internetbookstore.dao.ShortenedBooksDao;

import java.util.List;

public class ShortenedBookController
{
    private static MutableLiveData<List<ShortenedBookInfo>> books;

    private ShortenedBookController(){}

    public static LiveData<List<ShortenedBookInfo>> getInstance()
    {
        if(books == null)
            books = new MutableLiveData<>();

        return books;
    }


}
