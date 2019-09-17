package com.donteco.internetbookstore.converters;

import android.util.Log;

import androidx.room.TypeConverter;

import com.donteco.internetbookstore.constants.ConstantsForApp;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FullBookInfoTypeConverter {

    @TypeConverter
    public static JsonObject fromString(String value) {
        return (value.equals(""))? new JsonObject(): new JsonParser().parse(value).getAsJsonObject();
    }

    @TypeConverter
    public static String fromJsonObject(JsonObject value)
    {
        try {
            return (value==null)? "": value.toString();
        }
        catch (Exception e) {
            Log.e(ConstantsForApp.LOG_TAG, "Exception caused by converting server response to json array. Exception ", e);
            return "";
        }
    }
}
