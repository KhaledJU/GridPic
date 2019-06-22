package com.example.gridpic.catAPI;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CatResponce {
    @SerializedName("url")
    String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
