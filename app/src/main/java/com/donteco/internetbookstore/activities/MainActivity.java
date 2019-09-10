package com.donteco.internetbookstore.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.donteco.internetbookstore.R;
import com.donteco.internetbookstore.books.BookInCart;
import com.donteco.internetbookstore.fragments.ShoppingCartFragment;
import com.donteco.internetbookstore.models.RepositoryViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends BaseActivity
{
    private TextView badge;
    private RepositoryViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getRidOfTopBar();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        NavController navController = Navigation.findNavController(this,
                R.id.main_navigation_host_fragment);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        BottomNavigationMenuView bottomNavigationMenuView =(BottomNavigationMenuView)
                bottomNavigationView.getChildAt(0);

        View view = bottomNavigationMenuView.getChildAt(1);
        BottomNavigationItemView itemView = (BottomNavigationItemView) view ;

        LayoutInflater.from(this)
                .inflate(R.layout.bottomnavigation_view_badge, itemView, true);

        badge = itemView.findViewById(R.id.notifications_badge);

        viewModel = ViewModelProviders.of(this).get(RepositoryViewModel.class);

        createBadgeListener();
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
