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
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.donteco.internetbookstore.R;
import com.donteco.internetbookstore.constants.ConstantsForApp;
import com.donteco.internetbookstore.constants.IntentKeys;
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_book_description);

        getRidOfTopBar();

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

        String checker = intent.getStringExtra(IntentKeys.FULL_BOOK_PRICE).trim();
        checker = (checker.length() == 0)? "none": checker;
        price.setText(checker);

        checker = intent.getStringExtra(IntentKeys.FULL_BOOK_TITLE).trim();
        checker = (checker.length() == 0)? "none": checker;
        title.setText(checker);

        checker = intent.getStringExtra(IntentKeys.FULL_BOOK_AUTHORS).trim();
        checker = (checker.length() == 0)? "none": "by " + checker;
        author.setText(checker);

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
        {
            checker = "none";
            description.setText(checker);
        }

        checker = intent.getStringExtra(IntentKeys.FULL_BOOK_LANGUAGE).trim();
        checker = (checker.length() == 0)? "none": "by " + checker;

        language.setText(intent.getStringExtra(IntentKeys.FULL_BOOK_LANGUAGE));

        subtitle.setText(intent.getStringExtra(IntentKeys.FULL_BOOK_SUBTITLE));
        System.out.println("Subtitle in intent " + intent.getStringExtra(IntentKeys.FULL_BOOK_SUBTITLE));
        publishers.setText(intent.getStringExtra(IntentKeys.FULL_BOOK_PUBLISHERS));
        isbn10.setText(intent.getStringExtra(IntentKeys.FULL_BOOK_ISBN10));
        isbn13.setText(String.valueOf( intent.getLongExtra(IntentKeys.FULL_BOOK_ISBN13, 0)) );
    }

    private String checkFroEmptyAnswer(String bookInfo){
        return null;
    }
}
