package com.donteco.internetbookstore.storage;

import com.donteco.internetbookstore.books.ShortenedBookInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Storage {

    private static List<ShortenedBookInfo> cart;
    private Storage(){}

    public static void createBooks() {
        cart = new ArrayList<>();
    }

    public static void addPackOfBooks(Collection<? extends ShortenedBookInfo> collection) {
        cart.addAll(collection);
    }

    public static List<ShortenedBookInfo> getPackOfBooks(int from, int to){
        return cart.subList(from, to);
    }

    public static List<ShortenedBookInfo> getCart(){
        return cart;
    }

}
