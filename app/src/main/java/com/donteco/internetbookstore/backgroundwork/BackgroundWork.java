package com.donteco.internetbookstore.backgroundwork;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.donteco.internetbookstore.books.BookInCart;
import com.donteco.internetbookstore.constants.ConstantsForApp;
import com.donteco.internetbookstore.constants.IntentKeys;
import com.donteco.internetbookstore.dao.BookInCartDao;
import com.donteco.internetbookstore.dao.CachedBookDao;
import com.donteco.internetbookstore.db.DataBase;
import com.donteco.internetbookstore.models.RepositoryViewModel;
import com.donteco.internetbookstore.notification.MyNotificationBuilder;
import com.donteco.internetbookstore.storage.Repository;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class BackgroundWork extends Worker
{

    public BackgroundWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork()
    {
        if (getDataFromBase())
            return Result.success();
        else
            return Result.failure();
    }

    private boolean getDataFromBase()
    {
        DataBase dataBase = DataBase.getDataBase(getApplicationContext());
        BookInCartDao bookInCartDao = dataBase.bookInCartDao();
        CartAsync cartAsync = new CartAsync(bookInCartDao);
        cartAsync.execute();

        List<BookInCart> cart;

        try {
            cart = cartAsync.get();
        } catch (ExecutionException | InterruptedException e) {
            Log.e(ConstantsForApp.LOG_TAG, "Error caused by getting data from base. Error itself ", e);
            return false;
        }

        if(cart == null || cart.size() == 0)
            return false;

        int amountOfBooks = 0;
        int i = 1;
        StringBuilder stringBuilder = new StringBuilder();

        for (BookInCart bookInCart : cart)
        {
            amountOfBooks += bookInCart.getAmount();
            if(i <= 5)
            {
                stringBuilder.append(String.format(Locale.ENGLISH,
                        "%d) %s x%d\n", i, bookInCart.getTitle(), bookInCart.getAmount()));
            }
            i++;
        }

        if(i > 5)
            stringBuilder.append("And more!");

        MyNotificationBuilder.createNotification(getApplicationContext(),
                String.format(Locale.ENGLISH, "You have %d unordered books in your cart!", amountOfBooks ),
                String.format("Click to see your cart \nYour cart:\n\n%s", stringBuilder.toString()),
                ConstantsForApp.SHOPPING_CART_FRAGMENT);

        return true;
    }

    private static class CartAsync extends AsyncTask<Void, Void, List<BookInCart>>
    {
        private final BookInCartDao dao;

        private CartAsync(BookInCartDao dao) {
            this.dao = dao;
        }

        @Override
        protected List<BookInCart> doInBackground(Void... voids) {
            return dao.getCartByAsync();
        }
    }
}
