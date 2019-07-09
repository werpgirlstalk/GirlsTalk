package com.example.sai.girlstalk.models;

public class GroupMessage
{
    private String message;
    private String dateSent;
    private UserProfile sender;

    public GroupMessage() {
    }

    public GroupMessage(String message, String dateSent, UserProfile sender) {
        this.message = message;
        this.dateSent = dateSent;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateSent() {
        return dateSent;
    }

    public void setDateSent(String dateSent) {
        this.dateSent = dateSent;
    }

    public UserProfile getSender() {
        return sender;
    }

    public void setSender(UserProfile sender) {
        this.sender = sender;
    }
}
