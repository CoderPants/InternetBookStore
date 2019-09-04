package com.donteco.internetbookstore.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.donteco.internetbookstore.R;
import com.donteco.internetbookstore.adapters.ShoppingCartAdapter;
import com.donteco.internetbookstore.constants.ConstantsForApp;

public class ShoppingCartFragment extends Fragment
{
    private Activity activity;

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

        setRecyclerView(view.findViewById(R.id.cart_activity_rv_chosen_books));
    }

    private void setRecyclerView(RecyclerView recyclerView)
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        recyclerView.setAdapter(new ShoppingCartAdapter());
    }
}
