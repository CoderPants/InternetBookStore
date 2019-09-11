package com.donteco.internetbookstore.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.donteco.internetbookstore.R;
import com.donteco.internetbookstore.books.ShortenedBookInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ChooseBooksAdapter extends RecyclerView.Adapter<ChooseBooksAdapter.ChooseBooksViewHolder>
{
    private List<ShortenedBookInfo> books;
    private ChooseAdapterCallBack callBack;

    public ChooseBooksAdapter(ChooseAdapterCallBack callBack)
    {
        this.callBack = callBack;
        books = new ArrayList<>();
    }

    public void setBooks(List<ShortenedBookInfo> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    public void addBooks(List<ShortenedBookInfo> newShortenedBookInfos){
        books.addAll(newShortenedBookInfos);
        notifyDataSetChanged();
    }

    public void clearList(){
        books.clear();

        notifyDataSetChanged();
    }

    public List<ShortenedBookInfo> getBooks() {
        return books;
    }

    @NonNull
    @Override
    public ChooseBooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_search_for_books_recycler_view_element, parent, false);
        return new ChooseBooksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseBooksViewHolder holder, int position) {
        holder.bind(books.get(position));
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class ChooseBooksViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView bookImage;
        private TextView bookTitle;
        private TextView bookSubTitle;
        private TextView bookPrice;
        private FrameLayout clickableLayout;

        public ChooseBooksViewHolder(@NonNull View itemView)
        {
            super(itemView);
            bookImage = itemView.findViewById(R.id.main_element_iv_book_icon);
            bookTitle = itemView.findViewById(R.id.main_element_book_title);
            bookSubTitle = itemView.findViewById(R.id.main_element_book_subtitle);
            bookPrice = itemView.findViewById(R.id.main_element_book_price);
            clickableLayout = itemView.findViewById(R.id.main_element_fl_clickable);
        }

        public void bind(ShortenedBookInfo shortenedBookInfo)
        {
            if(shortenedBookInfo.getImageUrl() == null)
                bookImage.setImageDrawable(callBack.onNoIconCondition());
            else
                Picasso.get()
                .load(shortenedBookInfo.getImageUrl())
                .into(bookImage);

            bookTitle.setText(shortenedBookInfo.getTitle());
            bookSubTitle.setText(shortenedBookInfo.getSubtitle());
            bookPrice.setText(shortenedBookInfo.getPrice());

            layoutLogic();
        }

        private void layoutLogic() {
            clickableLayout.setOnClickListener(view ->
                    callBack.uploadFullBookInfo(books.get(getAdapterPosition()).getId()));
        }
    }

    public interface ChooseAdapterCallBack
    {
        Drawable onNoIconCondition();
        void uploadFullBookInfo(long id);
    }
}
