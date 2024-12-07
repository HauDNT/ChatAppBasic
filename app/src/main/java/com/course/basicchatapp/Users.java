package com.course.basicchatapp;

public class Users {
    String id, email, password, profileImage, lastMessage, status;

    public Users() {}

    public Users(String id, String email, String password, String profileImage, String status) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.profileImage = profileImage;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
