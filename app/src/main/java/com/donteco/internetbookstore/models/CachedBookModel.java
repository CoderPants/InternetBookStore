package com.donteco.internetbookstore.models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.donteco.internetbookstore.books.CachedBook;
import com.donteco.internetbookstore.storage.CachedRepository;

import java.util.List;

public class CachedBookModel extends AndroidViewModel
{

    private CachedRepository cachedRepository;

    public CachedBookModel(@NonNull Application application) {
        super(application);
        cachedRepository = new CachedRepository(application);
    }

    public void insert(List<CachedBook> cachedBooks) {
        cachedRepository.insert(cachedBooks);
    }
}
