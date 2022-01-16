package com.example.dotaapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Guide implements Parcelable {

    private int id;
    private String title;
    private String string;
    private String username;
    private String location;

    protected Guide(Parcel in) {
        id = in.readInt();
        title = in.readString();
        string = in.readString();
        username = in.readString();
        location = in.readString();
    }
    public Guide(){}

    public Guide(int id, String title, String string, String username, String location){
        this.id = id;
        this.title = title;
        this.string = string;
        this.username = username;
        this.location = location;
    }

    public static final Creator<Guide> CREATOR = new Creator<Guide>() {
        @Override
        public Guide createFromParcel(Parcel in) {
            return new Guide(in);
        }

        @Override
        public Guide[] newArray(int size) {
            return new Guide[size];
        }
    };

    public int getId() { return id; }

    public void setId(int id) { this.id = id;}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLocation(){return location;}

    public void setLocation(String location){this.location = location;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(string);
        dest.writeString(username);
        dest.writeString(location);
    }
}
