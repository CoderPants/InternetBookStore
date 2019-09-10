package com.donteco.internetbookstore.storage;

import android.content.Context;

import com.donteco.internetbookstore.constants.ConstantsForApp;

public class Storage {

    private static android.content.SharedPreferences sharedPreferences;
    private static Storage storage;

    private static String userInput;
    private static int lastRVPosition;

    private Storage(Context context) {
        sharedPreferences = context.getSharedPreferences(ConstantsForApp.SHARED_PREFERENCES_FILE_NAME,
                Context.MODE_PRIVATE);

        lastRVPosition = -1;
    }

    public static void loadStorage(Context context){
        if(storage == null)
            storage = new Storage(context);
    }

    public static void pushAllToStorage(){
        pushUserInputToStorage();
        pushLastPositionToStorage();
    }

    public static void pullAllFromStorage(){

        pullUserInputFromStorage();
        pullLastPositionFromStorage();
    }

    //----------------------------------------------------------------------------------------------
    public static String getUserInput() {
        return userInput;
    }

    public static void setUserInput(String userInput) {
        Storage.userInput = userInput;
        pushUserInputToStorage();
    }

    private static void pullUserInputFromStorage()
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

    //---------------------------------------------------------------------------------------------

    public static int getLastRVPosition() {
        return lastRVPosition;
    }

    public static void setLastRVPosition(int lastRVPosition) {
        Storage.lastRVPosition = lastRVPosition;
        pushLastPositionToStorage();
    }

    private static void pullLastPositionFromStorage()
    {
        String json = sharedPreferences.getString(ConstantsForApp.KEY_FOR_LAST_RV_POSITION, "");
        if(lastRVPosition == -1 && json.length() != 0)
            lastRVPosition = Integer.valueOf(json);
    }

    private static void pushLastPositionToStorage()
    {
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ConstantsForApp.KEY_FOR_LAST_RV_POSITION, String.valueOf(lastRVPosition));
        editor.apply();
    }
}
