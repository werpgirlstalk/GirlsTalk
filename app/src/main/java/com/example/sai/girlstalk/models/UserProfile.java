package com.example.sai.girlstalk.models;

public class UserProfile {
    private String profilePictureUrl;
    private String description;
    private String email;

    public UserProfile() {
    }

    public UserProfile(String profilePictureUrl, String description, String email) {
        this.profilePictureUrl = profilePictureUrl;
        this.description = description;
        this.email = email;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
