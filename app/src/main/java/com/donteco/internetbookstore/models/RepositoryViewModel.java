package com.donteco.internetbookstore.models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.donteco.internetbookstore.books.BookInCart;
import com.donteco.internetbookstore.books.CachedBook;
import com.donteco.internetbookstore.books.FullBookInfo;
import com.donteco.internetbookstore.books.ShortenedBookInfo;
import com.donteco.internetbookstore.storage.Repository;

import java.util.List;

public class RepositoryViewModel extends AndroidViewModel {
    private Repository repository;

    private MutableLiveData<String> userInputLiveData;
    private LiveData<List<ShortenedBookInfo>> shortenedInfoBooks;

    public RepositoryViewModel(@NonNull Application application) {
        super(application);

        repository = Repository.getInstance(application);

        userInputLiveData = new MutableLiveData<>();

        shortenedInfoBooks = Transformations.switchMap(userInputLiveData,
                input -> repository.getShortenedBooksInfoByRequest(input));
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

    public LiveData<FullBookInfo> getFullBookInfoById(long id){
        return repository.getFullBookInfoById(id);
    }

    public void setuserInputLiveData(String userInput) {
        userInputLiveData.setValue(userInput);
    }

    public LiveData<List<ShortenedBookInfo>> getShortenedInfoBooks(){
        return shortenedInfoBooks;
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

    public LiveData<BookInCart> getBookInCartById(long id){
        return repository.getBookInCartById(id);
    }

    public LiveData<List<BookInCart>> getCart(){
        return repository.getCart();
    }

}
