package com.donteco.internetbookstore.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.donteco.internetbookstore.R;
import com.donteco.internetbookstore.books.ShortenedBookInfo;
import com.donteco.internetbookstore.storage.ShoppingCart;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ShopCartViewHolder>
{
    private List<ShortenedBookInfo> shortenedBookInfos;

    public ShoppingCartAdapter() {
        shortenedBookInfos = ShoppingCart.getCart();
    }


    @NonNull
    @Override
    public ShopCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shopping_cart_activity_recycler_view_element, parent, false);
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
        private TextView deleteBtn;

        public ShopCartViewHolder(@NonNull View itemView) {
            super(itemView);

            bookImage = itemView.findViewById(R.id.shopping_cart_element_iv_book_icon);
            bookTitle = itemView.findViewById(R.id.shopping_cart_book_title);
            bookAuthor = itemView.findViewById(R.id.shopping_cart_book_author);
            bookPrice = itemView.findViewById(R.id.shopping_cart_book_price);
            deleteBtn = itemView.findViewById(R.id.shopping_cart_delete_from_cart_bnt);
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
        }
    }
}
