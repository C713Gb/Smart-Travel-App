package com.example.smarttravel.Models;

public class Song {

    private String currentTitle;
    private String currentArtist;

    public Song(String currentTitle, String currentArtist) {
        this.currentTitle = currentTitle;
        this.currentArtist = currentArtist;
    }

    public Song() {
    }

    public String getCurrentTitle() {
        return currentTitle;
    }

    public void setCurrentTitle(String currentTitle) {
        this.currentTitle = currentTitle;
    }

    public String getCurrentArtist() {
        return currentArtist;
    }

    public void setCurrentArtist(String currentArtist) {
        this.currentArtist = currentArtist;
    }
}
