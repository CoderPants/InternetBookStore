package com.donteco.internetbookstore.converters;

import androidx.room.TypeConverter;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FullBookInfoTypeConverter {

    @TypeConverter
    public static JsonObject fromString(String value) {
        System.out.println("Value " + value);
        return (value.equals(""))? new JsonObject(): new JsonParser().parse(value).getAsJsonObject();
    }

    @TypeConverter
    public static String fromJsonObject(JsonObject value){
        return (value == null)? "": value.getAsString();
    }
}
