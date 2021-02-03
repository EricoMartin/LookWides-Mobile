package com.mobilefintech09.lookwides.entities;

import com.squareup.moshi.Json;

public class ImageResponse {
    @Json(name = "Url")
    private String url;
    @Json(name = "Message")
    private String message;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
