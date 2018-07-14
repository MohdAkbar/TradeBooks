package com.example.akbar.tradebooks;

/**
 * Created by Akbar on 6/30/2018.
 */
public class Book {

    private String name;
    private String author;
    private String publishedYear;
    private String description;
    private String publication;
    private String genre;
    private String photoUrl;
    private int price;
    private int hits=0;//No. of times bought
    private int views=0;//No. of times viewed
    private int added=0;//No. of times added to Cart
    private String sellerId;
    private String buyerId="";
    private boolean sold=false;
    private String bookId="";

    public Book()
    {

    }

    public Book(String name, String author, String publishedYear, String description, String publication, String genre,String photoUrl, int price, String sellerId) {
        this.name = name;
        this.author = author;
        this.publishedYear = publishedYear;
        this.description = description;
        this.publication = publication;
        this.genre = genre;
        this.photoUrl=photoUrl;
        this.price = price;
        this.sellerId = sellerId;
        this.hits=0;
        this.views=0;
        this.added=0;
        this.buyerId="";
        this.sold=false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublishedYear(String publishedYear) {
        this.publishedYear = publishedYear;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPublication(String publication) {
        this.publication = publication;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public void setAdded(int added) {
        this.added = added;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getPublishedYear() {
        return publishedYear;
    }

    public String getPublication() {
        return publication;
    }

    public String getGenre() {
        return genre;
    }

    public int getPrice() {
        return price;
    }

    public int getViews() {
        return views;
    }

    public int getHits() {
        return hits;
    }

    public int getAdded() {
        return added;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public boolean isSold() {
        return sold;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
}
