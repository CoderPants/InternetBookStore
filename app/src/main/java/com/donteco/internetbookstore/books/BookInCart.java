package com.donteco.internetbookstore.books;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity (tableName = "shopping_cart")
public class BookInCart
{
    @PrimaryKey
    private long id;

    private String title;
    private String price;
    private int amount;
    @ColumnInfo(name = "url")
    private String imageUrl;

    @Ignore
    private double numericalPrice;

    public BookInCart(long id, String title, String price, int amount, String imageUrl) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.amount = amount;
        this.imageUrl = imageUrl;
        numericalPrice = Double.valueOf(price.substring(1));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getNumericalPrice() {
        return numericalPrice;
    }

    public void setNumericalPrice(int numericalPrice) {
        this.numericalPrice = numericalPrice;
    }

    //Get only first 2 digits after dot
    public double getTotalPrice(){
        return Math.floor(numericalPrice * amount * 100) / 100;
    }

    @Override
    public String toString() {
        return "BookInCart{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price='" + price + '\'' +
                ", amount=" + amount +
                ", imageUrl='" + imageUrl + '\'' +
                ", numericalPrice=" + numericalPrice +
                '}';
    }
}
