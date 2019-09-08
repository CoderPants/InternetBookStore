package com.donteco.internetbookstore.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.donteco.internetbookstore.R;
import com.donteco.internetbookstore.books.BookInCart;
import com.donteco.internetbookstore.constants.ConstantsForApp;
import com.donteco.internetbookstore.constants.IntentKeys;
import com.donteco.internetbookstore.models.RepositoryViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

public class FullBookDescription extends BaseActivity {

    private ImageView bookImage;
    private TextView pageAmount;
    private TextView publishDate;
    private RatingBar ratingBar;
    private TextView price;
    private TextView language;
    private TextView title;
    private TextView author;
    private TextView description;

    private TextView subtitle;
    private TextView publishers;
    private TextView isbn10;
    private TextView isbn13;

    private RepositoryViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_book_description);

        getRidOfTopBar();

        viewModel = ViewModelProviders.of(this).get(RepositoryViewModel.class);

        bookImage = findViewById(R.id.full_book_activity_iv_for_book_image);
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

        fillActivity();

        FloatingActionButton addBtn = findViewById(R.id.full_book_description_fab_for_adding_to_cart);

        if(viewModel.isBookInCart(getIntent().getLongExtra(IntentKeys.FULL_BOOK_ISBN13, 0)))
            addToCartLogic(addBtn);
        else
            disableBtn(addBtn);

        goBackLogic( findViewById(R.id.full_book_description_go_back) );
    }


    private void goBackLogic(RelativeLayout returnToMain) {
        returnToMain.setOnClickListener(view ->
                startActivity(new Intent(FullBookDescription.this, MainActivity.class)));
    }

    //Need to fix logic!!!
    private void addToCartLogic(FloatingActionButton addToCartBtn)
    {
        addToCartBtn.setOnClickListener(view ->
        {
            Intent intent = getIntent();

            viewModel.insertBookToCart(
                    new BookInCart( intent.getIntExtra(IntentKeys.FULL_BOOK_ISBN13, 0),
                            checkFroEmptyAnswer(intent.getStringExtra(IntentKeys.FULL_BOOK_TITLE)),
                            checkFroEmptyAnswer(intent.getStringExtra(IntentKeys.FULL_BOOK_PRICE)),
                            1,
                            checkFroEmptyAnswer(intent.getStringExtra(IntentKeys.FULL_BOOK_IMAGE))) );

            disableBtn(addToCartBtn);
            /*long bookId = getIntent().getLongExtra(IntentKeys.FULL_BOOK_ISBN13, 0);
            try{
                Storage.addToCart( viewModel.getShortenedBookInfo(bookId) );
                Toast.makeText(getApplicationContext(), "Book successfully added to the cart!", Toast.LENGTH_SHORT).show();
            }
            catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Something wrong, contact us.", Toast.LENGTH_SHORT).show();
            }*/
        });
    }

    private void fillActivity()
    {
        Intent intent = getIntent();

        try
        {
            Picasso.get()
                    .load(intent.getStringExtra(IntentKeys.FULL_BOOK_IMAGE))
                    .into(bookImage);
        }
        catch (Exception e)
        {
            Log.e(ConstantsForApp.LOG_TAG,
                    "Internet problem or no book icon. Exception caught in fill activity method" +
                            " in FullBookDescription activity. Exception: ", e);
            bookImage.setImageDrawable(getDrawable(R.drawable.no_book_image_icon));
        }

        pageAmount.setText( String.valueOf(intent.getIntExtra(IntentKeys.FULL_BOOK_PAGES, 0)) );
        publishDate.setText( String.valueOf(intent.getIntExtra(IntentKeys.FULL_BOOK_YEAR, 0)) );
        ratingBar.setRating(intent.getIntExtra(IntentKeys.FULL_BOOK_RATING, 0));

        price.setText( checkFroEmptyAnswer(intent.getStringExtra(IntentKeys.FULL_BOOK_PRICE)) );
        title.setText( checkFroEmptyAnswer(intent.getStringExtra(IntentKeys.FULL_BOOK_TITLE)) );

        String checker = checkFroEmptyAnswer(intent.getStringExtra(IntentKeys.FULL_BOOK_AUTHORS));
        author.setText( checker.equals("none")? checker: "by " + checker);

        checker = intent.getStringExtra(IntentKeys.FULL_BOOK_DESCRIPTION).trim();

        if(checker.length() != 0)
        {
            checker = checker.replace("...", "").trim();

            StringBuilder descriptionLink = new StringBuilder(checker);
            String link = "<a href='"+ intent.getStringExtra(IntentKeys.FULL_BOOK_URL) +"'>...</a>";

            descriptionLink = descriptionLink.append(link);
            description.setMovementMethod(LinkMovementMethod.getInstance());
            description.setText(Html.fromHtml(descriptionLink.toString()));
        }
        else
            description.setText("none");

        language.setText( checkFroEmptyAnswer(intent.getStringExtra(IntentKeys.FULL_BOOK_LANGUAGE)) );
        subtitle.setText( checkFroEmptyAnswer(intent.getStringExtra(IntentKeys.FULL_BOOK_SUBTITLE)) );
        publishers.setText( checkFroEmptyAnswer(intent.getStringExtra(IntentKeys.FULL_BOOK_PUBLISHERS)) );
        isbn10.setText( checkFroEmptyAnswer(intent.getStringExtra(IntentKeys.FULL_BOOK_ISBN10)) );

        isbn13.setText(String.valueOf( intent.getLongExtra(IntentKeys.FULL_BOOK_ISBN13, 0)) );
    }

    private String checkFroEmptyAnswer(@NonNull String bookInfo) {
        return (bookInfo.trim().length() == 0)? "none": bookInfo;
    }

    private void disableBtn(FloatingActionButton btn){
        btn.setClickable(false);
        btn.setImageDrawable(getDrawable(R.drawable.ic_check_black_24dp));
    }
}
