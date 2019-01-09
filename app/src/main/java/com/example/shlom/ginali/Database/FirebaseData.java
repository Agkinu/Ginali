package com.example.shlom.ginali.Database;

public class FirebaseData {

    private String dogFriendly;
    private String handicapFriendly;

    public FirebaseData() {
    }

    public FirebaseData(String dogFriendly, String handicapFriendly) {
        this.dogFriendly = dogFriendly;
        this.handicapFriendly = handicapFriendly;
    }

    public String getDogFriendly() {
        return dogFriendly;
    }

    public void setDogFriendly(String dogFriendly) {
        this.dogFriendly = dogFriendly;
    }

    public String getHandicapFriendly() {
        return handicapFriendly;
    }

    public void setHandicapFriendly(String handicapFriendly) {
        this.handicapFriendly = handicapFriendly;
    }
}
