package com.donteco.internetbookstore.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.donteco.internetbookstore.R;
import com.donteco.internetbookstore.books.BookInCart;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ShopCartViewHolder>
{
    private List<BookInCart> shoppingCart;
    private CallBack callBack;

    public ShoppingCartAdapter(CallBack callBack) {
        shoppingCart = new ArrayList<>();
        this.callBack = callBack;
        //shoppingCart = Storage.getBooksInCart();
    }

    public List<BookInCart> getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(List<BookInCart> shoppingCart) {
        this.shoppingCart = shoppingCart;
        notifyDataSetChanged();
    }

    public void deleteBook(int position)
    {
        callBack.deleteBookInCart(shoppingCart.get(position));
        shoppingCart.remove(position);
        notifyItemRemoved(position);
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
        holder.bind(shoppingCart.get(position));
    }

    @Override
    public int getItemCount() {
        return shoppingCart.size();
    }

    public class ShopCartViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView bookImage;
        private TextView bookTitle;
        private TextView bookPrice;

        private ImageView addOneMoreBook;
        private TextView amountOfBooksAndPrice;
        private ImageView deleteOneBook;

        public ShopCartViewHolder(@NonNull View itemView) {
            super(itemView);

            bookImage = itemView.findViewById(R.id.shopping_cart_element_iv_book_icon);
            bookTitle = itemView.findViewById(R.id.shopping_cart_book_title);
            bookPrice = itemView.findViewById(R.id.shopping_cart_book_price);

            addOneMoreBook = itemView.findViewById(R.id.shopping_cart_add_book_btn);
            amountOfBooksAndPrice = itemView.findViewById(R.id.shopping_cart_book_amount_and_price);
            deleteOneBook = itemView.findViewById(R.id.shopping_cart_delete_book_btn);
        }

        public void bind(BookInCart bookInCart)
        {
            if(bookInCart.getImageUrl() != null)
                Picasso.get()
                        .load(bookInCart.getImageUrl())
                        .into(bookImage);

            bookTitle.setText(bookInCart.getTitle());
            //bookAuthor.setText(shortenedBookInfo.getSubtitle());
            /*String textForTv = bookInCart.getAmount() + " X " + bookInCart.getPrice();
            amountOfBooksAndPrice.setText(textForTv);*/
            setPriceAndAmount(bookInCart);

            String bookValue =  bookInCart.getTotalPrice() + "$";
            bookPrice.setText(bookValue);

            addOneBookLogic(bookInCart);
            deleteOneBookLogic(bookInCart);
        }

        private void deleteOneBookLogic(BookInCart bookInCart)
        {
            deleteOneBook.setOnClickListener(view ->
            {
                int amount = bookInCart.getAmount()-1;

                if(amount > 0)
                {
                    bookInCart.setAmount(amount);
                    setPriceAndAmount(bookInCart);
                    //amountOfBooks.setText(String.valueOf(amount));
                    setBookPrice(bookInCart);
                    callBack.updateBookInCartInfo(bookInCart);
                }
            });
        }

        private void addOneBookLogic(BookInCart bookInCart)
        {
            addOneMoreBook.setOnClickListener(view ->
            {
                int amount = bookInCart.getAmount()+1;
                bookInCart.setAmount(amount);
                setPriceAndAmount(bookInCart);
                //amountOfBooks.setText(String.valueOf(amount));
                setBookPrice(bookInCart);
                callBack.updateBookInCartInfo(bookInCart);
            });
        }

        private void setBookPrice(BookInCart bookInCart){
            String bookValue =  bookInCart.getTotalPrice() + "$";
            bookPrice.setText(bookValue);
        }

        private void setPriceAndAmount(BookInCart bookInCart){
            String textForTv = bookInCart.getAmount() + " X " + bookInCart.getPrice();
            amountOfBooksAndPrice.setText(textForTv);
        }
    }

    public interface CallBack{
        void updateBookInCartInfo(BookInCart bookInCart);
        void deleteBookInCart(BookInCart bookInCart);
    }
}
