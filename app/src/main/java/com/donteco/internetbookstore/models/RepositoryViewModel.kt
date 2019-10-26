package com.donteco.internetbookstore.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations

import com.donteco.internetbookstore.books.BookInCart
import com.donteco.internetbookstore.books.CachedBook
import com.donteco.internetbookstore.books.FullBookInfo
import com.donteco.internetbookstore.books.ShortenedBookInfo
import com.donteco.internetbookstore.storage.Repository

class RepositoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Repository = Repository.getInstance(application)

    private val userInputLiveData: MutableLiveData<String> = MutableLiveData()
    val shortenedInfoBooks: LiveData<List<ShortenedBookInfo>>

    val cart: LiveData<List<BookInCart>>
        get() = repository.cart

    init {
        shortenedInfoBooks = Transformations.switchMap(userInputLiveData) {
            input -> repository.getShortenedBooksInfoByRequest(input) }
    }

    fun insertShortenedBooksInfoAsync(books: List<ShortenedBookInfo>) = repository.insertShortenedBooksInfoAsync(books)

    fun insertCachedBooksInfoAsync(books: List<CachedBook>) = repository.insertCachedBooksInfoAsync(books)

    fun insertFullBookInfoAsync(fullBookInfo: FullBookInfo) = repository.insertFullBookInfoAsync(fullBookInfo)


    fun getFullBookInfoById(id: Long): LiveData<FullBookInfo> {
        return repository.getFullBookInfoById(id)
    }

    fun setUserInputLiveData(userInput: String) {
        userInputLiveData.value = userInput
    }

    fun insertBookToCartAsync(bookInCart: BookInCart) = repository.insertBookInCartInfoAsync(bookInCart)

    fun updateBookInCartAsync(bookInCart: BookInCart) = repository.updateBookInCartInfoAsync(bookInCart)

    fun deleteBookInCartAsync(bookInCart: BookInCart) = repository.deleteBookInCartAsync(bookInCart)

    fun getBookInCartById(id: Long): LiveData<BookInCart> {
        return repository.getBookInCartById(id)
    }
}
