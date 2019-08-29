package com.donteco.internetbookstore.books;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cached_books")
public class CachedBook
{
    @PrimaryKey(autoGenerate = true)
    private long generatedID;
    private String userRequest;
    @ColumnInfo(name = "id")
    private long bookID;

    public CachedBook(String userRequest, long bookID) {
        this.userRequest = userRequest;
        this.bookID = bookID;
    }

    public long getGeneratedID() {
        return generatedID;
    }

    public void setGeneratedID(long generatedID) {
        this.generatedID = generatedID;
    }

    public String getUserRequest() {
        return userRequest;
    }

    public void setUserRequest(String userRequest) {
        this.userRequest = userRequest;
    }

    public long getBookID() {
        return bookID;
    }

    public void setBookID(long bookID) {
        this.bookID = bookID;
    }
}
