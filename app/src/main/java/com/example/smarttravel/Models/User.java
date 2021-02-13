package com.example.smarttravel.Models;

public class User {

    private String userId;
    private String username;
    private String userEmail;
    private String userPic;

    public User() {
    }

    public User(String userId, String username, String userEmail, String userPic) {
        this.userId = userId;
        this.username = username;
        this.userEmail = userEmail;
        this.userPic = userPic;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
