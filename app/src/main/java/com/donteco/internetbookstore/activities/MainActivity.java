package com.donteco.internetbookstore.activities;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.donteco.internetbookstore.R;
import com.donteco.internetbookstore.adapters.ChooseBooksAdapter;
import com.donteco.internetbookstore.books.CachedBook;
import com.donteco.internetbookstore.books.FullBookInfo;
import com.donteco.internetbookstore.books.ShortenedBookInfo;
import com.donteco.internetbookstore.dialogs.FullBookDescriptionDialog;
import com.donteco.internetbookstore.models.RepositoryViewModel;
import com.donteco.internetbookstore.request.RequestSender;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity
{
    private ImageButton goToCart;
    private EditText searchBar;
    private ProgressBar loadingIndicator;
    private String userInput;

    private RepositoryViewModel viewModel;
    private RequestSender requestSender;
    private ChooseBooksAdapter chooseBooksAdapter;

    private BottomNavigationView bottomNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationBar = findViewById(R.id.bottom_navigation);
        bottomNavigationBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.book_search_bottom_navigation:
                        Toast.makeText(MainActivity.this, "Books search ", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.shopping_cart_bottom_navigation:
                        Toast.makeText(MainActivity.this, "Shopping cart ", Toast.LENGTH_SHORT).show();
                        break;
                }

                return true;
            }
        });

        getRidOfTopBar();

        goToCart = findViewById(R.id.show_cart_btn);
        goToCartLogic();

        //Models
        viewModel = ViewModelProviders.of(this).get(RepositoryViewModel.class);

        loadingIndicator = findViewById(R.id.main_activity_pb_loading_indicator);

        setRecyclerView(findViewById(R.id.rv_books));

        searchBar = findViewById(R.id.book_search_bar);
        ImageView imageView = findViewById(R.id.book_search_iv);
        setSearchIVLogic(imageView);

        requestSender = new RequestSender(new RequestSender.RequestCallBack()
        {
            @Override
            public void onGetBooksResponse(List<ShortenedBookInfo> receivedShortenedBooksInfo)
            {
                if(noListInDB(receivedShortenedBooksInfo,
                        chooseBooksAdapter.getBooks(),
                        requestSender.getTotalNumberOfBooks() % 10))
                {
                    viewModel.insertShortenedBooksInfo(receivedShortenedBooksInfo);
                    viewModel.insertCachedBooksInfo( convertToCachedBooks(receivedShortenedBooksInfo, userInput) );
                }
                loadingIndicator.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onGetBookInfoResponse(FullBookInfo fullBookInfo)
            {
                loadingIndicator.setVisibility(View.INVISIBLE);

                viewModel.insertFullBookInfo(fullBookInfo);

                FullBookDescriptionDialog dialog = new FullBookDescriptionDialog(MainActivity.this, fullBookInfo);
                dialog.show();
            }

            @Override
            public void onNoBooksCondition() {
                Toast.makeText(MainActivity.this, "Sorry, no books found!", Toast.LENGTH_LONG).show();
            }

        });
    }

    private void setSearchIVLogic(ImageView imageView) {
        imageView.setOnClickListener(view ->
        {
            chooseBooksAdapter.clearList();

            userInput = searchBar.getText().toString();

            if(isNetworkAvailable())
                requestSender.sentGetTotalBooksAmount(userInput);

            viewModel.getBooksByUserRequest(userInput).observe(this, shortenedBookInfos ->
            {
                chooseBooksAdapter.setBooks(shortenedBookInfos);

                if(chooseBooksAdapter.getItemCount() == 0)
                    loadingIndicator.setVisibility(View.VISIBLE);
                else
                    loadingIndicator.setVisibility(View.INVISIBLE);
            });
        });
    }

    private void goToCartLogic()
    {
        goToCart.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ShoppingCartActivity.class);
            startActivity(intent);
        });
    }

    private void setRecyclerView(RecyclerView recyclerView)
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        chooseBooksAdapter = new ChooseBooksAdapter(new ChooseBooksAdapter.ChooseAdapterCallBack() {
            @Override
            public Drawable onNoIconCondition() {
                return getResources().getDrawable(R.drawable.no_book_image_icon, null);
            }

            @Override
            public void uploadFullBookInfo(long id) {
                loadingIndicator.setVisibility(View.VISIBLE);
                FullBookInfo fullBookInfo = viewModel.getFullBookInfoByRequest(id);

                if(fullBookInfo == null)
                    requestSender.sentGetFullBookInfo(id);
                else
                {
                    FullBookDescriptionDialog dialog = new FullBookDescriptionDialog(MainActivity.this, fullBookInfo);
                    dialog.show();
                    loadingIndicator.setVisibility(View.INVISIBLE);
                }
            }
        });

        recyclerView.setAdapter(chooseBooksAdapter);
    }

}
