package com.example.sai.girlstalk.models;

public class GroupModel {

    private String groupIcon;
    private String groupMembers;
    private String groupLocation;
    private String groupTitle;

    public String getGroupIcon() {
        return groupIcon;
    }

    public void setGroupIcon(String groupIcon) {
        this.groupIcon = groupIcon;
    }

    public String getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(String groupMembers) {
        this.groupMembers = groupMembers;
    }

    public String getGroupLocation() {
        return groupLocation;
    }

    public void setGroupLocation(String groupLocation) {
        this.groupLocation = groupLocation;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public GroupModel(String groupIcon, String groupMembers, String groupLocation, String groupTitle) {
        this.groupIcon = groupIcon;
        this.groupMembers = groupMembers;
        this.groupLocation = groupLocation;
        this.groupTitle = groupTitle;
    }
}
