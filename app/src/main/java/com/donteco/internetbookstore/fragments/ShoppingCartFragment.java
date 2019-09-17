package com.donteco.internetbookstore.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.donteco.internetbookstore.R;
import com.donteco.internetbookstore.activities.FullBookDescription;
import com.donteco.internetbookstore.adapters.ShoppingCartAdapter;
import com.donteco.internetbookstore.backgroundwork.BackgroundWork;
import com.donteco.internetbookstore.books.BookInCart;
import com.donteco.internetbookstore.constants.ConstantsForApp;
import com.donteco.internetbookstore.constants.IntentKeys;
import com.donteco.internetbookstore.helper.SwipeToDeleteCallBack;
import com.donteco.internetbookstore.models.RepositoryViewModel;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ShoppingCartFragment extends Fragment
{
    private Activity activity;
    private RepositoryViewModel viewModel;
    private ShoppingCartAdapter adapter;

    private TextView totalAmountOfBooks;
    private TextView totalAmountOfMoney;
    private double amountOfMoney;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        try {
            activity = getActivity();
            return inflater.inflate(R.layout.fragment_shopping_cart, container, false);
        }
        catch (Exception e)
        {
            Log.wtf(ConstantsForApp.LOG_TAG, "Error occurred int shoppingCartFragment in onCreateView method. Exception ", e);
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        totalAmountOfMoney = view.findViewById(R.id.cart_fragment_tv_for_total_price);
        totalAmountOfBooks = view.findViewById(R.id.cart_fragment_tv_top);

        viewModel = ViewModelProviders.of(this).get(RepositoryViewModel.class);
        adapter = new ShoppingCartAdapter(new ShoppingCartAdapter.CallBack() {
            @Override
            public void updateBookInCartInfo(BookInCart bookInCart) {
                viewModel.updateBookInCart(bookInCart);
            }

            @Override
            public void deleteBookInCart(BookInCart bookInCart) {
                viewModel.deleteBookInCart(bookInCart);
            }

            @Override
            public void uploadFullBookInfo(long id)
            {
                Intent intent = new Intent(activity, FullBookDescription.class);
                intent.putExtra(IntentKeys.FULL_BOOK_ISBN13, id);
                startActivity(intent);
            }

            @Override
            public Drawable getBackGround(boolean isCLicked) {
                return activity.getResources().getDrawable(
                        (isCLicked)? R.drawable.recycler_view_element_background_pressed:
                                R.drawable.recycler_view_element_background_stable, null);
            }
        });

        setRecyclerView(view.findViewById(R.id.cart_fragment_rv_chosen_books));
    }

    private void setViewModel() {
        viewModel.getCart().observe(ShoppingCartFragment.this, bookInCarts ->
        {
            adapter.setShoppingCart(bookInCarts);
            setTotalAmount();
        });
    }

    private void setRecyclerView(RecyclerView recyclerView)
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new SwipeToDeleteCallBack(adapter,
                        getResources().getDrawable(R.drawable.ic_delete_white_24dp, null)));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onResume() {
        setViewModel();
        super.onResume();
    }

    @Override
    public void onStop() {
        /*PeriodicWorkRequest myWorkRequest = new PeriodicWorkRequest.Builder(BackgroundWork.class,
                30, TimeUnit.MINUTES, 25, TimeUnit.MINUTES)
                .build();*/
        Log.d(ConstantsForApp.LOG_TAG, "Passed on stop ");
        //Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
        OneTimeWorkRequest myWorkRequest = new OneTimeWorkRequest.Builder(BackgroundWork.class)
                .setInitialDelay(10, TimeUnit.SECONDS)
                .build();
        //WorkManager.getInstance(activity).enqueueUniqueWork("Work", ExistingWorkPolicy.REPLACE, myWorkRequest);
        WorkManager.getInstance(activity).enqueue(myWorkRequest);

        super.onStop();
    }

    private void setTotalAmount() {
        List<BookInCart> cart = adapter.getShoppingCart();
        amountOfMoney = 0;
        int amountOfBooks = 0;

        for (BookInCart bookInCart : cart)
        {
            amountOfMoney += bookInCart.getTotalPrice();
            amountOfBooks += bookInCart.getAmount();
        }

        formatAmount();
        String updatedAmount = amountOfMoney + "$";
        totalAmountOfMoney.setText(updatedAmount);

        String amountOfBooksInCart = "My Shopping cart (" + amountOfBooks + ")";
        totalAmountOfBooks.setText(amountOfBooksInCart);
    }

    private void formatAmount(){
        amountOfMoney = Math.floor(amountOfMoney *100) / 100;
    }
}
