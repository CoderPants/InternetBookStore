package com.donteco.internetbookstore.books;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "full_books_description")
public class FullBookInfo
{
    private int error;
    private String title;
    private String subtitle;
    private String authors;
    private String publisher;
    private String language;
    //'Cos there was some examples with strings
    private String isbn10;

    @SerializedName("isbn13")
    @PrimaryKey
    private long id;

    private int pages;
    private int year;
    private int rating;
    @SerializedName("desc")
    private String description;
    private String price;
    private String image;
    private String url;
    private JsonObject pdf;

    public FullBookInfo(int error, String title, String subtitle, String authors, String publisher,
                        String language, String isbn10, long id, int pages, int year, int rating,
                        String description, String price, String image, String url, JsonObject pdf)
    {
        this.error = error;
        this.title = title;
        this.subtitle = subtitle;
        this.authors = authors;
        this.publisher = publisher;
        this.language = language;
        this.isbn10 = isbn10;
        this.id = id;
        this.pages = pages;
        this.year = year;
        this.rating = rating;
        this.description = description;
        this.price = price;
        this.image = image;
        this.url = url;
        this.pdf = pdf;
    }


    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
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

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(String isbn10) {
        this.isbn10 = isbn10;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public JsonObject getPdf() {
        return pdf;
    }

    public void setPdf(JsonObject pdf) {
        this.pdf = pdf;
    }

    @Override
    public String toString() {
        return "FullBookInfo{" +
                "error=" + error +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", authors='" + authors + '\'' +
                ", publisher='" + publisher + '\'' +
                ", language='" + language + '\'' +
                ", isbn10=" + isbn10 +
                ", id=" + id +
                ", pages=" + pages +
                ", year=" + year +
                ", rating=" + rating +
                ", description='" + description + '\'' +
                ", price='" + price + '\'' +
                ", image='" + image + '\'' +
                ", url='" + url + '\'' +
                ", pdf=" + pdf +
                '}';
    }
}
