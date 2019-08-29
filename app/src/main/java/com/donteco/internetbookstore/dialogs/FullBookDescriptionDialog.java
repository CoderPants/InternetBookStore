package com.donteco.internetbookstore.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.donteco.internetbookstore.R;
import com.donteco.internetbookstore.books.FullBookInfo;
import com.squareup.picasso.Picasso;

public class FullBookDescriptionDialog extends Dialog implements View.OnClickListener
{
    private TextView cancelButton;
    private ImageView bookImage;
    private TextView price;
    private RatingBar rating;
    private TextView authors;
    private TextView publishers;
    private TextView publishedYear;
    private TextView pageAmount;
    private TextView language;
    private TextView description;
    private Button buyBtn;
    private Button previewBtn;

    private FullBookInfo fullBookInfo;

    public FullBookDescriptionDialog(@NonNull Context context, FullBookInfo fullBookInfo) {
        super(context);
        this.fullBookInfo = fullBookInfo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.full_book_info_dialog);

        cancelButton = findViewById(R.id.full_book_info_cancel_btn);
        bookImage = findViewById(R.id.full_book_info_iv_book_image);
        price = findViewById(R.id.full_book_info_tv_book_price);
        rating = findViewById(R.id.full_book_info_rating_bar);
        authors = findViewById(R.id.full_book_info_tv_book_authors);
        publishers = findViewById(R.id.full_book_info_tv_book_publishers);
        publishedYear = findViewById(R.id.full_book_info_tv_book_publish_year);
        pageAmount = findViewById(R.id.full_book_info_tv_book_pages);
        language = findViewById(R.id.full_book_info_tv_book_language);
        description = findViewById(R.id.full_book_info_tv_description);
        buyBtn = findViewById(R.id.full_book_info_buy_btn);
        previewBtn = findViewById(R.id.full_book_info_preview_btn);

        fillDialog();

        cancelButton.setOnClickListener(this);
        buyBtn.setOnClickListener(this);
        previewBtn.setOnClickListener(this);
    }

    private void fillDialog()
    {
        //Check no image condition
        Picasso.get()
                .load(fullBookInfo.getImage())
                .into(bookImage);

        StringBuilder description = new StringBuilder(fullBookInfo.getDescription());
        String link = "<a href='"+ fullBookInfo.getUrl() +"'>See all</a>";

        description = description.append(link);

        price.setText(fullBookInfo.getPrice());
        rating.setRating(fullBookInfo.getRating());
        authors.setText(fullBookInfo.getAuthors());
        publishers.setText(fullBookInfo.getPublisher());
        publishedYear.setText(String.valueOf(fullBookInfo.getYear()));
        pageAmount.setText(String.valueOf(fullBookInfo.getPages()));
        language.setText(fullBookInfo.getLanguage());
        this.description.setMovementMethod(LinkMovementMethod.getInstance());
        this.description.setText(Html.fromHtml(description.toString()));
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.full_book_info_cancel_btn:
                dismiss();
                break;

            case R.id.full_book_info_buy_btn:
                System.out.println("Pressed buy");
                break;

            case R.id.full_book_info_preview_btn:
                System.out.println("Pressed preview");
                break;
        }

    }
}
