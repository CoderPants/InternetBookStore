package com.donteco.internetbookstore.storage;

import android.content.Context;

import com.donteco.internetbookstore.constants.ConstantsForApp;

public class Storage {

    private static android.content.SharedPreferences sharedPreferences;
    private static Storage storage;

    private static String userInput;

    private Storage(Context context) {
        sharedPreferences = context.getSharedPreferences(ConstantsForApp.SHARED_PREFERENCES_FILE_NAME,
                Context.MODE_PRIVATE);
    }

    public static void loadStorage(Context context){
        if(storage == null)
            storage = new Storage(context);
    }

    public static void pushAllToStorage(){
        pushUserInputToStorage();
    }

    public static void pullAllFromStorage(){
        pullUserInputFormStorage();
    }

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
