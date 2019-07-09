package com.example.sai.girlstalk.models;

public class User
{
    private String username;
    private String password;
    private UserProfile profile;

    public User() {}

    public User(String username, String email,String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String email, String password, UserProfile profile) {
        this.username = username;
        this.password = password;
        this.profile = profile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }
}
