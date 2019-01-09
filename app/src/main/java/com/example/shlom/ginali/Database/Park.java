package com.example.shlom.ginali.Database;

public class Park {

    private String name;
    private Double latitude;
    private Double longitude;
    private String placeId;
    private String address;
    private String photo;
    private Double rating;
    private long id;
    private String distance;
    private String route;
    private boolean favorite;



    public Park() {
    }

    public Park(long id, boolean favorite) {
        this.id = id;
        this.favorite = favorite;
    }

    public Park(String name, Double latitude, Double longitude, String route) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.route = route;
    }

    public Park(String name, Double latitude, Double longitude, String placeId, String address, String photo, Double rating, String distance) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeId = placeId;
        this.address = address;
        this.photo = photo;
        this.rating = rating;
        this.distance = distance;
    }

    public Park(String name, Double latitude, Double longitude, String placeId, String address) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeId = placeId;
        this.address = address;
    }

    public Park(String name, Double latitude, Double longitude, String placeId, String address, long id) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeId = placeId;
        this.address = address;
        this.id = id;
    }

    public Park(String name, Double latitude, Double longitude, String placeId, String address, long id,String photo, Double rating) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeId = placeId;
        this.address = address;
        this.id = id;
        this.photo = photo;
        this.rating = rating;
    }

    public Park(String name, Double latitude, Double longitude,  String placeId, String address, String photo, Double rating) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeId = placeId;
        this.address = address;
        this.photo = photo;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
