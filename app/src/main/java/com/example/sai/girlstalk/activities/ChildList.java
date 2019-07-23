package com.example.sai.girlstalk.activities;

import android.os.Parcel;
import android.os.Parcelable;

public class ChildList implements Parcelable {

    public static final Creator<ChildList> CREATOR = new Creator<ChildList>() {
        @Override
        public ChildList createFromParcel(Parcel in) {
            return new ChildList(in);
        }

        @Override
        public ChildList[] newArray(int size) {
            return new ChildList[size];
        }
    };
    private String title;
    private String body;

    public ChildList(String title, String body) {
        this.title = title;
        this.body = body;
    }

    protected ChildList(Parcel in) {
        title = in.readString();
        body = in.readString();
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String Title) {
        this.title = Title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(body);
    }
}