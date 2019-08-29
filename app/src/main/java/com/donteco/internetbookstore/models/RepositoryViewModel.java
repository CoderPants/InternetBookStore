package com.donteco.internetbookstore.models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.donteco.internetbookstore.books.CachedBook;
import com.donteco.internetbookstore.books.FullBookInfo;
import com.donteco.internetbookstore.books.ShortenedBookInfo;
import com.donteco.internetbookstore.storage.Repository;

import java.util.List;

public class RepositoryViewModel extends AndroidViewModel
{
    private Repository repository;

    public RepositoryViewModel(@NonNull Application application) {
        super(application);

        repository = new Repository(application);
    }

    public void insertShortenedBooksInfo(List<ShortenedBookInfo> books){
        repository.insertShortenedBooksInfo(books);
    }

    public void insertCachedBooksInfo(List<CachedBook> books){
        repository.insertCachedBooksInfo(books);
    }

    public void insertFullBookInfo(FullBookInfo fullBookInfo){
        repository.insertFullBookInfo(fullBookInfo);
    }

    public FullBookInfo getFullBookInfoByRequest(int id){
        return repository.getFullBookInfo(id);
    }

    public LiveData< List<ShortenedBookInfo> > getBooksByUserRequest(String request) {
        return repository.getShortenedBooksInfoByRequest(request);
    }
}
