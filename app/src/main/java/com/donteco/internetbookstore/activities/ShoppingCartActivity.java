package com.donteco.internetbookstore.activities;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.donteco.internetbookstore.R;
import com.donteco.internetbookstore.adapters.ShoppingCartAdapter;

public class ShoppingCartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        getRidOfTopBar();

        setRecyclerView(findViewById(R.id.cart_activity_rv_chosen_books));
    }

    private void setRecyclerView(RecyclerView recyclerView)
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(new ShoppingCartAdapter());
    }
}
