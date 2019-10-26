package com.donteco.internetbookstore.storage

import android.app.Application
import android.os.AsyncTask
import android.util.Log

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.donteco.internetbookstore.books.BookInCart
import com.donteco.internetbookstore.books.CachedBook
import com.donteco.internetbookstore.books.FullBookInfo
import com.donteco.internetbookstore.books.ShortenedBookInfo
import com.donteco.internetbookstore.dao.BookInCartDao
import com.donteco.internetbookstore.dao.CachedBookDao
import com.donteco.internetbookstore.dao.FullInfoBooksDao
import com.donteco.internetbookstore.dao.ShortenedBooksDao
import com.donteco.internetbookstore.db.DataBase
import com.donteco.internetbookstore.constants.ConstantsForApp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.concurrent.ExecutionException

class Repository private constructor(application: Application) {
    private val shortenedBooksDao: ShortenedBooksDao
    private val fullInfoBooksDao: FullInfoBooksDao
    private val cachedBookDao: CachedBookDao
    private val bookInCartDao: BookInCartDao

    val cart: LiveData<List<BookInCart>>
        get() = bookInCartDao.cart

    init {
        val dataBase = DataBase.getDataBase(application)
        shortenedBooksDao = dataBase.shortenedBooksDao()
        fullInfoBooksDao = dataBase.fullInfoBooksDao()
        cachedBookDao = dataBase.cachedBookDao()
        bookInCartDao = dataBase.bookInCartDao()
    }

    fun insertShortenedBooksInfoAsync(books: List<ShortenedBookInfo>) = GlobalScope.async {
        shortenedBooksDao.insertBooks(books)
        // Thread { shortenedBooksDao.insertBooks(books) }.start()
    }

    fun insertFullBookInfoAsync(book: FullBookInfo) = GlobalScope.async {
        fullInfoBooksDao.insertFullBook(book)
        //Thread { fullInfoBooksDao.insertFullBook(book) }.start()
    }

    fun insertCachedBooksInfoAsync(cachedBooks: List<CachedBook>) = GlobalScope.async{
        cachedBookDao.insert(cachedBooks)
        // Thread { cachedBookDao.insert(cachedBooks) }.start()
    }

    fun insertBookInCartInfoAsync(bookInCart: BookInCart) = GlobalScope.async{
        bookInCartDao.insert(bookInCart)
        // Thread { bookInCartDao.insert(bookInCart) }.start()
    }

    fun updateBookInCartInfoAsync(bookInCart: BookInCart) = GlobalScope.async{
        bookInCartDao.update(bookInCart)
        //Thread { bookInCartDao.update(bookInCart) }.start()
    }

    fun deleteBookInCartAsync(bookInCart: BookInCart) = GlobalScope.async{
        bookInCartDao.delete(bookInCart)
        // Thread { bookInCartDao.delete(bookInCart) }.start()
    }

    fun getBookInCartById(id: Long): LiveData<BookInCart> {
        return bookInCartDao.getBookInCartById(id)
    }

    fun getShortenedBooksInfoByRequest(request: String): LiveData<List<ShortenedBookInfo>> {
        return shortenedBooksDao.getShortenedBooksByRequest("%$request%")
    }

    fun getFullBookInfoById(id: Long): LiveData<FullBookInfo> {
        return fullInfoBooksDao.getFullBookInfoById(id)
    }

    //Singleton
    companion object {

        private var repository: Repository? = null

        fun getInstance(application: Application): Repository {
            if (repository == null)
                synchronized(Repository::class.java) {
                    if (repository == null)
                        repository = Repository(application)

                }
            return repository!!
        }
    }

}
