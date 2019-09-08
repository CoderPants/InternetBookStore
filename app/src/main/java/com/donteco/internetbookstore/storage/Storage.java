package com.donteco.internetbookstore.storage;

import android.content.Context;

import com.donteco.internetbookstore.books.ShortenedBookInfo;
import com.donteco.internetbookstore.constants.ConstantsForApp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Storage {

    private static android.content.SharedPreferences sharedPreferences;
    private static Storage storage;

    private static String userInput;

    private Storage(Context context) {
        sharedPreferences = context.getSharedPreferences(ConstantsForApp.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
    }

    public static void loadStorage(Context context){
        if(storage == null)
            storage = new Storage(context);
    }

    public static void pushAllToStorage(){
        //pushCartToStorage();
        pushUserInputToStorage();
    }

    public static void pullAllFromStorage(){
        //pullCartFromStorage();
        pullUserInputFormStorage();
    }

   /* //----------------------------------------------------------------------------------------------
   public static void addToCart(ShortenedBookInfo shortenedBookInfo)
   {
       if(booksInCart == null)
           booksInCart = new ArrayList<>();

       booksInCart.add(shortenedBookInfo);
   }

    public static List<ShortenedBookInfo> getBooksInCart(){
       if(booksInCart == null)
           booksInCart = new ArrayList<>();

       return booksInCart;
    }

    private static void pullCartFromStorage()
    {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(ConstantsForApp.KEY_FOR_STORED_CART, "");
        Type type;

        if (json.isEmpty())
            return;

        type = new TypeToken<List<ShortenedBookInfo>>(){}.getType();

        if (booksInCart == null)
            booksInCart = gson.fromJson(json, type);
    }

    private static void pushCartToStorage()
    {
        Gson gson = new Gson();
        String jsonString = gson.toJson(booksInCart);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ConstantsForApp.KEY_FOR_STORED_CART, jsonString);
        editor.apply();
    }*/

    //----------------------------------------------------------------------------------------------
    public static String getUserInput() {
        return userInput;
    }

    public static void setUserInput(String userInput) {
        Storage.userInput = userInput;
        pushUserInputToStorage();
    }

    private static void pullUserInputFormStorage()
    {
        String json = sharedPreferences.getString(ConstantsForApp.KEY_FOR_USER_INPUT, "");
        if(userInput == null)
            userInput = json;
    }

    private static void pushUserInputToStorage()
    {
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ConstantsForApp.KEY_FOR_USER_INPUT, userInput);
        editor.apply();
    }
}
