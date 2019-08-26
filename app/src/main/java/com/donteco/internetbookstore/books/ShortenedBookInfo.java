package com.donteco.internetbookstore.books;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "shortened_book_description")
public class ShortenedBookInfo
{
    private String title;
    private String subtitle;

    @SerializedName("isbn13")
    @PrimaryKey
    private long id;

    private String price;

    @SerializedName("image")
    private String imageUrl;

    @SerializedName("url")
    private String link;

    public ShortenedBookInfo(String title, String subtitle, long id, String price, String imageUrl, String link) {
        this.title = title;
        this.subtitle = subtitle;
        this.id = id;
        this.price = price;
        this.imageUrl = imageUrl;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "ShortenedBookInfo{" +
                "title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", id=" + id +
                ", price='" + price + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
