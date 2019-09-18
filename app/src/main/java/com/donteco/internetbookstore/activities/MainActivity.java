package com.donteco.internetbookstore.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.donteco.internetbookstore.R;
import com.donteco.internetbookstore.adapters.ShoppingCartAdapter;
import com.donteco.internetbookstore.books.BookInCart;
import com.donteco.internetbookstore.constants.ConstantsForApp;
import com.donteco.internetbookstore.constants.IntentKeys;
import com.donteco.internetbookstore.fragments.SearchBooksFragment;
import com.donteco.internetbookstore.fragments.ShoppingCartFragment;
import com.donteco.internetbookstore.models.RepositoryViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends BaseActivity
{
    private TextView badge;
    private RepositoryViewModel viewModel;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getRidOfTopBar();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        navController = Navigation.findNavController(this,
                R.id.main_navigation_host_fragment);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        //Create red dot with amount of books in cart
        BottomNavigationMenuView bottomNavigationMenuView =(BottomNavigationMenuView)
                bottomNavigationView.getChildAt(0);

        View view = bottomNavigationMenuView.getChildAt(1);
        BottomNavigationItemView itemView = (BottomNavigationItemView) view ;

        LayoutInflater.from(this)
                .inflate(R.layout.bottomnavigation_view_badge, itemView, true);

        badge = itemView.findViewById(R.id.notifications_badge);

        viewModel = ViewModelProviders.of(this).get(RepositoryViewModel.class);

        createBadgeListener();

        //Redirect intent data to fragment
        redirectToFragment();
    }

    private void redirectToFragment()
    {
        String fragment = getIntent().getStringExtra(IntentKeys.PUSH_NOTIFICATION);

        if(fragment != null && fragment.equals(ConstantsForApp.SEARCH_FRAGMENT))
        {
            Bundle bundle = new Bundle();
            bundle.putBoolean(IntentKeys.NEED_NEW_BOOKS, true);
            navController.navigate(R.id.book_search_bottom_navigation, bundle);
        }

    }

    private void createBadgeListener() {
        viewModel.getCart().observe(this, bookInCarts ->
        {
            int amountOfBooksInCart = 0;

            for (BookInCart bookInCart : bookInCarts)
                amountOfBooksInCart += bookInCart.getAmount();

            if(amountOfBooksInCart == 0)
                badge.setVisibility(View.INVISIBLE);
            else
            {
                badge.setVisibility(View.VISIBLE);
                String badgeString = (amountOfBooksInCart >= 100)? "99+": String.valueOf(amountOfBooksInCart);
                badge.setText(badgeString);
            }
        });
    }
}
