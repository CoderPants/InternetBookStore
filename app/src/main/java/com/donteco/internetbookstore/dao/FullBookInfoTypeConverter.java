package com.donteco.internetbookstore.dao;

import androidx.room.TypeConverter;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FullBookInfoTypeConverter {

    @TypeConverter
    public static JsonObject fromString(String value) {
        return new JsonParser().parse(value).getAsJsonObject();
    }

    @TypeConverter
    public static String fromJsonObject(JsonObject value){
        return value.getAsString();
    }
}
