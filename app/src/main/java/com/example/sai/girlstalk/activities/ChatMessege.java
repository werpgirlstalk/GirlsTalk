package com.example.sai.girlstalk.activities;

import java.util.Date;

public class ChatMessege {
    private String MessgeText;
    private String MessegeUser;
    private long MessegeTime;

    public ChatMessege(String messgeText, String messegeUser) {
        MessgeText = messgeText;
        MessegeUser = messegeUser;

        MessegeTime = new Date().getTime();
    }

    public ChatMessege() {
    }

    public String getMessgeText() {
        return MessgeText;
    }

    public void setMessgeText(String messgeText) {
        MessgeText = messgeText;
    }

    public String getMessegeUser() {
        return MessegeUser;
    }

    public void setMessegeUser(String messegeUser) {
        MessegeUser = messegeUser;
    }

    public long getMessegeTime() {
        return MessegeTime;
    }

    public void setMessegeTime(long messegeTime) {
        MessegeTime = messegeTime;
    }
}
