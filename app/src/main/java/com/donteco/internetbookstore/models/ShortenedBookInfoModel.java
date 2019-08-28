package com.donteco.internetbookstore.models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.donteco.internetbookstore.books.ShortenedBookInfo;
import com.donteco.internetbookstore.storage.ShortenedInfoRepository;

import java.util.List;

public class ShortenedBookInfoModel extends AndroidViewModel
{
    private ShortenedInfoRepository repository;

    public ShortenedBookInfoModel(@NonNull Application application) {
        super(application);

        repository = new ShortenedInfoRepository(application);
    }

    public void insert(List<ShortenedBookInfo> books){
        repository.insert(books);
    }

    public LiveData< List<ShortenedBookInfo> > getBooksByUserRequest(String request) {
        return repository.getBooksByRequest(request);
    }
}
