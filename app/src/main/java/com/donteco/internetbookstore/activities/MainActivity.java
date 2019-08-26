package com.donteco.internetbookstore.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.donteco.internetbookstore.R;
import com.donteco.internetbookstore.adapters.ChooseBooksAdapter;
import com.donteco.internetbookstore.books.FullBookInfo;
import com.donteco.internetbookstore.books.ShortenedBookInfo;
import com.donteco.internetbookstore.dialogs.FullBookDescriptionDialog;
import com.donteco.internetbookstore.help.ActivityHelper;
import com.donteco.internetbookstore.help.ConstantsForApp;
import com.donteco.internetbookstore.request.RequestSender;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageButton goToCart;
    private EditText searchBar;
    private ProgressBar loadingIndicator;


    private RequestSender requestSender;
    private int searchPageNumber;

    private ChooseBooksAdapter chooseBooksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goToCart = findViewById(R.id.show_cart_btn);
        goToCartLogic();

        loadingIndicator = findViewById(R.id.main_activity_pb_loading_indicator);

        ActivityHelper.getRidOfTopBar(this);

        setRecyclerView( findViewById(R.id.rv_books));

        searchBar = findViewById(R.id.book_search_bar);
        ImageView imageView = findViewById(R.id.book_search_iv);
        setSearchIVLogic(imageView);

        requestSender = new RequestSender(new RequestSender.RequestCallBack()
        {
            @Override
            public void onGetBooksResponse(List<ShortenedBookInfo> receivedShortenedBooksInfo)
            {
                chooseBooksAdapter.addBooks(receivedShortenedBooksInfo);
                loadingIndicator.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onGetBookInfoResponse(FullBookInfo fullBookInfo)
            {
                loadingIndicator.setVisibility(View.INVISIBLE);
                FullBookDescriptionDialog dialog = new FullBookDescriptionDialog(MainActivity.this, fullBookInfo);
                dialog.show();
            }
        });
    }

    private void setSearchIVLogic(ImageView imageView) {
        imageView.setOnClickListener(view ->
        {
            chooseBooksAdapter.clearList();

            loadingIndicator.setVisibility(View.VISIBLE);
            //Here we set new one

            searchPageNumber = 1;
            requestSender.sentGetBooksRequest(searchBar.getText().toString(), searchPageNumber);
        });

    }

    private void goToCartLogic() {
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
            public void uploadAnotherPageOfBooks() {
                searchPageNumber++;

                if(checkIfCanRequest())
                {
                    loadingIndicator.setVisibility(View.VISIBLE);
                    requestSender.sentGetBooksRequest(searchBar.getText().toString(), searchPageNumber);
                }
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
        return ( requestSender.getTotalNumberOfBooks() -
                searchPageNumber*ConstantsForApp.AMOUNT_OF_BOOKS_PER_PAGE )
                > -ConstantsForApp.AMOUNT_OF_BOOKS_PER_PAGE;
    }
}
