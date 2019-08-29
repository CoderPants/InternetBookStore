package com.donteco.internetbookstore.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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
import com.donteco.internetbookstore.help.ActivityHelper;
import com.donteco.internetbookstore.help.ConstantsForApp;
import com.donteco.internetbookstore.models.RepositoryViewModel;
import com.donteco.internetbookstore.request.RequestSender;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageButton goToCart;
    private EditText searchBar;
    private ProgressBar loadingIndicator;

    private RepositoryViewModel viewModel;

    private RequestSender requestSender;
    private int searchPageNumber;

    private String userInput;

    private ChooseBooksAdapter chooseBooksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goToCart = findViewById(R.id.show_cart_btn);
        goToCartLogic();

        //Models
        viewModel = ViewModelProviders.of(this).get(RepositoryViewModel.class);

        loadingIndicator = findViewById(R.id.main_activity_pb_loading_indicator);

        ActivityHelper.getRidOfTopBar(this);

        setRecyclerView(findViewById(R.id.rv_books));

        searchBar = findViewById(R.id.book_search_bar);
        ImageView imageView = findViewById(R.id.book_search_iv);
        setSearchIVLogic(imageView);

        requestSender = new RequestSender(new RequestSender.RequestCallBack() {
            @Override
            public void onGetBooksResponse(List<ShortenedBookInfo> receivedShortenedBooksInfo) {
                //chooseBooksAdapter.addBooks(receivedShortenedBooksInfo);
                System.out.println("Got into ongetBooksResponce "+receivedShortenedBooksInfo.toString());
                viewModel.insertShortenedBooksInfo(receivedShortenedBooksInfo);
                viewModel.insertCachedBooksInfo(convertToCachedBooks(receivedShortenedBooksInfo));
                loadingIndicator.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onGetBookInfoResponse(FullBookInfo fullBookInfo) {
                loadingIndicator.setVisibility(View.INVISIBLE);
                FullBookDescriptionDialog dialog = new FullBookDescriptionDialog(MainActivity.this, fullBookInfo);
                dialog.show();
            }

            @Override
            public void onNoBooksCondition() {
                Toast.makeText(MainActivity.this, "No books added!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private List<CachedBook> convertToCachedBooks(List<ShortenedBookInfo> receivedShortenedBooksInfo) {
        List<CachedBook> result = new ArrayList<>();

        for (ShortenedBookInfo shortenedBookInfo : receivedShortenedBooksInfo)
               result.add(new CachedBook(userInput, shortenedBookInfo.getId()));

        return result;
    }

    private void setSearchIVLogic(ImageView imageView) {
        imageView.setOnClickListener(view ->
        {
            chooseBooksAdapter.clearList();

            System.out.println("Got into search logic!");
            searchPageNumber = 1;
            userInput = searchBar.getText().toString();

            if(isNetworkAvailable())
                requestSender.sentGetTotalBooksAmount(userInput);

            viewModel.getBooksByUserRequest(userInput).observe(this, shortenedBookInfos ->
            {
                System.out.println("Got into observer ");
                chooseBooksAdapter.setBooks(shortenedBookInfos);

                if(chooseBooksAdapter.getItemCount() == 0)
                    loadingIndicator.setVisibility(View.VISIBLE);
                else
                    loadingIndicator.setVisibility(View.INVISIBLE);
            });
        });

    }

    private void goToCartLogic() {
        goToCart.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ShoppingCartActivity.class);
            startActivity(intent);
        });
    }

    private void setRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        chooseBooksAdapter = new ChooseBooksAdapter(new ChooseBooksAdapter.ChooseAdapterCallBack() {
            @Override
            public Drawable onNoIconCondition() {
                return getResources().getDrawable(R.drawable.no_book_image_icon, null);
            }

            @Override
            public void uploadAnotherPageOfBooks()
            {
                /*if (checkIfCanRequest())
                {
                    searchPageNumber++;
                    loadingIndicator.setVisibility(View.VISIBLE);
                    requestSender.sentGetBooksRequest(searchBar.getText().toString(), searchPageNumber);
                }*/
            }

            @Override
            public void uploadFullBookInfo(long id) {
                loadingIndicator.setVisibility(View.VISIBLE);
                requestSender.sentGetFullBookInfo(id);
            }
        });

        recyclerView.setAdapter(chooseBooksAdapter);
    }

    private boolean checkIfCanRequest() {
        System.out.println("Total amount of books " + requestSender.getTotalNumberOfBooks() + " SearchPage number " + searchPageNumber + " amount of books in adapter " + chooseBooksAdapter.getItemCount());
        return (requestSender.getTotalNumberOfBooks() -
                searchPageNumber * ConstantsForApp.AMOUNT_OF_BOOKS_PER_PAGE)
                > -ConstantsForApp.AMOUNT_OF_BOOKS_PER_PAGE;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
