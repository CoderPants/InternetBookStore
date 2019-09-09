package com.donteco.internetbookstore.models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.donteco.internetbookstore.books.BookInCart;
import com.donteco.internetbookstore.books.CachedBook;
import com.donteco.internetbookstore.books.FullBookInfo;
import com.donteco.internetbookstore.books.ShortenedBookInfo;
import com.donteco.internetbookstore.storage.Repository;

import java.util.List;

public class RepositoryViewModel extends AndroidViewModel {
    private Repository repository;

    private Application application;

    public RepositoryViewModel(@NonNull Application application) {
        super(application);

        this.application = application;

        repository = new Repository(application);
    }

    public void insertShortenedBooksInfo(List<ShortenedBookInfo> books) {
        repository.insertShortenedBooksInfo(books);
    }

    public void insertCachedBooksInfo(List<CachedBook> books) {
        repository.insertCachedBooksInfo(books);
    }

    public void insertFullBookInfo(FullBookInfo fullBookInfo) {
        repository.insertFullBookInfo(fullBookInfo);
    }

    public FullBookInfo getFullBookInfoByRequest(long id) {
        return repository.getFullBookInfo(id);
    }

    public LiveData<List<ShortenedBookInfo>> getBooksByUserRequest(String request) {
        return repository.getShortenedBooksInfoByRequest(request);
    }

    public void insertBookToCart(BookInCart bookInCart){
        repository.insertBookInCartInfo(bookInCart);
    }

    public void updateBookInCart(BookInCart bookInCart){
        repository.updateBookInCartInfo(bookInCart);
    }

    public void deleteBookInCart(BookInCart bookInCart){
        repository.deleteBookInCart(bookInCart);
    }

    /*public boolean isBookInCart(long id){
        System.out.println("Book in db " + repository.getBookInCartById(id));
        return repository.getBookInCartById(id) != null;
    }*/

    public LiveData<BookInCart> getBookInCartById(long id){
        return repository.getBookInCartById(id);
    }

    public LiveData<List<BookInCart>> getCart(){
        return repository.getCart();
    }


}
