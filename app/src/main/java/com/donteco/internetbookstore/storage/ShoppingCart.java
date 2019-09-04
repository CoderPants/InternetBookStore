package com.donteco.internetbookstore.storage;

import com.donteco.internetbookstore.books.ShortenedBookInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ShoppingCart {

    private static List<ShortenedBookInfo> cart;
    private ShoppingCart(){}

    public static void createBooks() {
        cart = new ArrayList<>();
    }

   /* public static void addPackOfBooks(Collection<? extends ShortenedBookInfo> collection) {
        cart.addAll(collection);
    }*/

   public static void addToCart(ShortenedBookInfo book) {
       cart.add(book);
   }


    public static List<ShortenedBookInfo> getCart(){
        return cart;
    }

}
