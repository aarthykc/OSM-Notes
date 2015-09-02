package com.example.aarthychandrasekhar.mapboxtest;

/**
 * Created by aarthychandrasekhar on 02/09/15.
 */
public class OSMComment {
    String user;
    String text;

    public OSMComment(String user, String text) {
        this.user = user;
        this.text = text;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
