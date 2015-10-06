package com.example.aarthychandrasekhar.mapboxtest;

/**
 * Created by aarthychandrasekhar on 28/09/15.
 */
public class Credentials {
    String username, password;

    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
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
}
