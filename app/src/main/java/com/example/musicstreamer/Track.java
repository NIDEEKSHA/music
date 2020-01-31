package com.example.musicstreamer;

public class Track {
   // private String company;
    private String title;
    private String artist;
    private int image;

    public Track(String title, String artist, int image) {
        //this.company = company;
        this.title = title;
        this.artist = artist;
        this.image = image;
    }

//    public void setCompany(String company) {
//        this.company = company;
//    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

//    public String getCompany() {
//        return company;
//    }
}
