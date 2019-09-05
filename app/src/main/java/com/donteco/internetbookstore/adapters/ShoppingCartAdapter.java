package com.donteco.internetbookstore.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.donteco.internetbookstore.R;
import com.donteco.internetbookstore.books.ShortenedBookInfo;
import com.donteco.internetbookstore.storage.Storage;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ShopCartViewHolder>
{
    private List<ShortenedBookInfo> shortenedBookInfos;

    public ShoppingCartAdapter() {
        shortenedBookInfos = Storage.getBooksInCart();
    }


    @NonNull
    @Override
    public ShopCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_shopping_cart_recycler_view_element, parent, false);
        return new ShopCartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopCartViewHolder holder, int position) {
        holder.bind(shortenedBookInfos.get(position));
    }

    @Override
    public int getItemCount() {
        return shortenedBookInfos.size();
    }

    public class ShopCartViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView bookImage;
        private TextView bookTitle;
        private TextView bookAuthor;
        private TextView bookPrice;

        private ImageView addOneMoreBook;
        private TextView amountOfBooks;
        private ImageView deleteOneBook;

        public ShopCartViewHolder(@NonNull View itemView) {
            super(itemView);

            bookImage = itemView.findViewById(R.id.shopping_cart_element_iv_book_icon);
            bookTitle = itemView.findViewById(R.id.shopping_cart_book_title);
            bookAuthor = itemView.findViewById(R.id.shopping_cart_book_author);
            bookPrice = itemView.findViewById(R.id.shopping_cart_book_price);

            addOneMoreBook = itemView.findViewById(R.id.shopping_cart_add_book_btn);
            amountOfBooks = itemView.findViewById(R.id.shopping_cart_tv_amount_of_books);
            deleteOneBook = itemView.findViewById(R.id.shopping_cart_delete_book_btn);
        }

        public void bind(ShortenedBookInfo shortenedBookInfo)
        {
            if(shortenedBookInfo.getImageUrl() != null)
                Picasso.get()
                        .load(shortenedBookInfo.getImageUrl())
                        .into(bookImage);

            bookTitle.setText(shortenedBookInfo.getTitle());
            bookAuthor.setText(shortenedBookInfo.getSubtitle());
            bookPrice.setText(shortenedBookInfo.getPrice());

            addOneBookLogic(shortenedBookInfo);
            deleteOneBookLogic(shortenedBookInfo);
        }

        private void deleteOneBookLogic(ShortenedBookInfo shortenedBookInfo) {
            deleteOneBook.setOnClickListener(view ->
            {
                int curAmountOfBooks = Integer.valueOf(amountOfBooks.getText().toString()) - 1;
                String price = shortenedBookInfo.getPrice();
                int curPrice = Integer.valueOf( price.substring(0, price.indexOf(" ")) ) * curAmountOfBooks;
                amountOfBooks.setText(String.valueOf(curAmountOfBooks));
                bookPrice.setText(String.valueOf(curPrice));
            });
        }

        private void addOneBookLogic(ShortenedBookInfo shortenedBookInfo) {

        }

        private void setBookPrice(ShortenedBookInfo shortenedBookInfo){

        }
    }
}
