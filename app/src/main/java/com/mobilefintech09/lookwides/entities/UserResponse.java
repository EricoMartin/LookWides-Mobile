package com.mobilefintech09.lookwides.entities;

import com.squareup.moshi.Json;

public class UserResponse {
    @Json(name = "User")
    private User user;
    @Json(name = "Message")
    private String message;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
