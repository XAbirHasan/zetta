package com.project.zetta.Model;

public class Ratings {
    String rating, comment;

    public Ratings() {
    }

    public Ratings(String rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
