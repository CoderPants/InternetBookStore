package com.donteco.internetbookstore.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.donteco.internetbookstore.R;
import com.donteco.internetbookstore.adapters.ShoppingCartAdapter;
import com.donteco.internetbookstore.books.BookInCart;
import com.donteco.internetbookstore.constants.ConstantsForApp;
import com.donteco.internetbookstore.models.RepositoryViewModel;

import java.util.List;

public class ShoppingCartFragment extends Fragment
{
    private Activity activity;
    private RepositoryViewModel viewModel;
    private ShoppingCartAdapter adapter;

    private TextView totalAmount;
    private double totalAmountNumerical;

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

        totalAmount = view.findViewById(R.id.cart_fragment_tv_for_total_price);

        viewModel = ViewModelProviders.of(this).get(RepositoryViewModel.class);
        adapter = new ShoppingCartAdapter(bookInCart ->
        {
            viewModel.updateBookInCart(bookInCart);

            /*totalAmountNumerical += bookInCart.getTotalPrice();
            formatAmount();
            String updatedAmount = totalAmountNumerical + "$";
            totalAmount.setText(updatedAmount);*/

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
    }

    @Override
    public void onResume() {
        setViewModel();

        //setTotalAmount();

        super.onResume();
    }

    private void setTotalAmount() {
        List<BookInCart> cart = adapter.getShoppingCart();
        totalAmountNumerical = 0;

        for (BookInCart bookInCart : cart)
            totalAmountNumerical += bookInCart.getTotalPrice();

        formatAmount();
        String updatedAmount = totalAmountNumerical + "$";
        totalAmount.setText(updatedAmount);
    }

    private void formatAmount(){
        totalAmountNumerical = Math.floor(totalAmountNumerical*100) / 100;
    }
}
