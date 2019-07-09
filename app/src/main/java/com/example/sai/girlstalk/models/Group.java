package com.example.sai.girlstalk.models;

import java.util.List;

public class Group {
    private String title;
    private String icon;
    private String description;
    private List<UserProfile> members;

    public Group() {}

    public Group(String title, String icon, String description,List<UserProfile> members) {
        this.title = title;
        this.icon = icon;
        this.description = description;
        this.members = members;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<UserProfile> getMembers() {
        return members;
    }

    public void setMembers(List<UserProfile> members) {
        this.members = members;
    }
}
