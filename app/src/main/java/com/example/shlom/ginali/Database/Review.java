package com.example.shlom.ginali.Database;

public class Review {

    String review;
    String date;
    float rating;

    public Review(String review, String date, float rating) {
        this.review = review;
        this.date = date;
        this.rating = rating;
    }

    public Review() {
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
