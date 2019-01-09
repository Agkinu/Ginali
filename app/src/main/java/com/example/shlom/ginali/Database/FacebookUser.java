package com.example.shlom.ginali.Database;

public class FacebookUser {

    String email;
    String photo;

    public FacebookUser(String email, String photo) {
        this.email = email;
        this.photo = photo;
    }

    public FacebookUser(String email) {
        this.email = email;
    }

    public FacebookUser() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
