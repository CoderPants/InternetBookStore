package com.donteco.internetbookstore.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.donteco.internetbookstore.R;
import com.donteco.internetbookstore.books.BookInCart;
import com.donteco.internetbookstore.books.FullBookInfo;
import com.donteco.internetbookstore.books.ShortenedBookInfo;
import com.donteco.internetbookstore.constants.ConstantsForApp;
import com.donteco.internetbookstore.constants.IntentKeys;
import com.donteco.internetbookstore.models.RepositoryViewModel;
import com.donteco.internetbookstore.request.RequestSender;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FullBookDescription extends BaseActivity {

    private ProgressBar loadingIndicator;

    private ImageView image;
    private TextView pageAmount;
    private TextView publishDate;
    private RatingBar ratingBar;
    private TextView price;
    private TextView language;
    private TextView title;
    private TextView author;
    private TextView description;

    private FloatingActionButton addBtn;

    private TextView subtitle;
    private TextView publishers;
    private TextView isbn10;
    private TextView isbn13;

    private RepositoryViewModel viewModel;
    private RequestSender requestSender;

    //Book data
    private long bookId;
    private String bookTitle;
    private String bookPrice;
    private String bookImage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_book_description);

        getRidOfTopBar();

        viewModel = ViewModelProviders.of(this).get(RepositoryViewModel.class);

        loadingIndicator = findViewById(R.id.full_book_description_activity_pb_loading_indicator);

        image = findViewById(R.id.full_book_activity_iv_for_book_image);
        pageAmount = findViewById(R.id.full_book_description_activity_tv_amount_of_pages);
        publishDate = findViewById(R.id.full_book_description_activity_tv_published_year);
        ratingBar = findViewById(R.id.full_book_description_activity_ratingBar);
        price = findViewById(R.id.full_book_description_activity_tv_price);
        language = findViewById(R.id.full_book_description_activity_tv_language);
        title = findViewById(R.id.full_book_description_nsv_tv_book_title);
        author = findViewById(R.id.full_book_description_nsv_tv_book_author);
        description = findViewById(R.id.full_book_description_nsv_ll_tv_description);
        subtitle = findViewById(R.id.full_book_description_nsv_tv_book_subtitle);
        publishers = findViewById(R.id.full_book_description_nsv_tv_book_publishers);
        isbn10 = findViewById(R.id.full_book_description_nsv_tv_book_isbn10);
        isbn13 = findViewById(R.id.full_book_description_nsv_tv_book_isbn13);

        //Changing starts color
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(this, R.color.black),
                PorterDuff.Mode.SRC_ATOP);

        requestSenderCreation();

        getFullBookInfo();

        addBtn = findViewById(R.id.full_book_description_fab_for_adding_to_cart);

        addToCartLogic(addBtn);

        goBackLogic( findViewById(R.id.full_book_description_go_back) );
    }

    private void getFullBookInfo() {
        loadingIndicator.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        long bookId = intent.getLongExtra(IntentKeys.FULL_BOOK_ISBN13, 0);

        viewModel.getFullBookInfoById(bookId).observe(this, fullBookInfo ->
        {
            if(fullBookInfo == null)
                requestSender.sentGetFullBookInfo(bookId);
            else
            {
                loadingIndicator.setVisibility(View.INVISIBLE);
                fillActivity(fullBookInfo);
            }
        });
    }

    private void requestSenderCreation() {
        requestSender = new RequestSender(new RequestSender.RequestCallBack() {
            @Override
            public void onGetBooksResponse(List<ShortenedBookInfo> receivedShortenedBooksInfo) {
                //Nothing
            }

            @Override
            public void onGetBookInfoResponse(FullBookInfo fullBookInfo) {
                loadingIndicator.setVisibility(View.INVISIBLE);
                fillActivity(fullBookInfo);
                viewModel.insertFullBookInfo(fullBookInfo);
            }

            @Override
            public void onNoBooksCondition() {
                loadingIndicator.setVisibility(View.INVISIBLE);
                Toast.makeText(FullBookDescription.this,
                        "Sorry, there is no book info on server!",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onGetNewBooks(List<ShortenedBookInfo> receivedShortenedBooksInfo) {
                //Nothing
            }
        });
    }

    private void checkIfBookInCart() {
        viewModel.getBookInCartById(bookId).observe(this, bookInCart ->
        {
            if(bookInCart != null)
                disableBtn(addBtn);
        });
    }


    private void goBackLogic(RelativeLayout returnToMain) {
        returnToMain.setOnClickListener(view -> onBackPressed());
    }

    private void addToCartLogic(FloatingActionButton addToCartBtn)
    {
        addToCartBtn.setOnClickListener(view ->
        {
            try
            {
                viewModel.insertBookToCart( new BookInCart( bookId, bookTitle, bookPrice, 1, bookImage) );

                Toast.makeText(getApplicationContext(), "Book was successfully added to the cart!", Toast.LENGTH_SHORT).show();
            }
            catch (Exception e)
            {
                Log.e(ConstantsForApp.LOG_TAG,
                        "Exception in FullBookDescription activity in addToCartLogic, " +
                                "caused by inserting data to db. Exception ", e);
                Toast.makeText(getApplicationContext(), "Something went wrong, contact us.", Toast.LENGTH_SHORT).show();
            }

            disableBtn(addToCartBtn);
        });
    }

    private void fillActivity(FullBookInfo fullBookInfo)
    {

        bookImage = fullBookInfo.getImage();
        try
        {
            Picasso.get()
                    .load(bookImage)
                    .into(image);
        }
        catch (Exception e)
        {
            Log.e(ConstantsForApp.LOG_TAG,
                    "Internet problem or no book icon. Exception caught in fill activity method" +
                            " in FullBookDescription activity. Exception: ", e);
            image.setImageDrawable(getDrawable(R.drawable.no_book_image_icon));
        }

        pageAmount.setText( String.valueOf(fullBookInfo.getPages()) );
        publishDate.setText( String.valueOf(fullBookInfo.getYear()) );
        ratingBar.setRating(fullBookInfo.getRating());

        bookTitle = fullBookInfo.getTitle();
        bookPrice = fullBookInfo.getPrice();

        price.setText( checkFroEmptyAnswer(bookPrice) );
        title.setText( checkFroEmptyAnswer(bookTitle) );

        String checker = checkFroEmptyAnswer(fullBookInfo.getAuthors());
        author.setText( checker.equals("none")? checker: "by " + checker);

        checker = fullBookInfo.getDescription().trim();

        if(checker.length() != 0)
        {
            checker = checker.replace("...", "").trim();

            StringBuilder descriptionLink = new StringBuilder(checker);
            String link = "<a href='"+ fullBookInfo.getUrl() +"'>...</a>";

            descriptionLink = descriptionLink.append(link);
            description.setMovementMethod(LinkMovementMethod.getInstance());
            description.setText(Html.fromHtml(descriptionLink.toString()));
        }
        else
            description.setText("none");

        language.setText( checkFroEmptyAnswer(fullBookInfo.getLanguage()) );
        subtitle.setText( checkFroEmptyAnswer(fullBookInfo.getSubtitle()) );
        publishers.setText( checkFroEmptyAnswer(fullBookInfo.getPublisher()) );
        isbn10.setText( checkFroEmptyAnswer(fullBookInfo.getIsbn10()) );

        bookId = fullBookInfo.getId();
        isbn13.setText(String.valueOf(bookId) );
        checkIfBookInCart();
    }

    private String checkFroEmptyAnswer(@NonNull String bookInfo) {
        return (bookInfo.trim().length() == 0)? "none": bookInfo;
    }

    private void disableBtn(FloatingActionButton btn){
        btn.setClickable(false);
        btn.setImageDrawable(getDrawable(R.drawable.ic_check_black_24dp));
    }
}
