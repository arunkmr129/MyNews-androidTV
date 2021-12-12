package com.example.mynewsandroidtv.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MyItemModel implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("vastURL")
    @Expose
    private String vastURL;
    @SerializedName("videoItems")
    @Expose
    private List<VideoItemModel> videoItemModelList = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVastURL() {
        return vastURL;
    }

    public void setVastURL(String vastURL) {
        this.vastURL = vastURL;
    }

    public List<VideoItemModel> getVideoItemModelList() {
        return videoItemModelList;
    }

    public void setVideoItemModelList(List<VideoItemModel> items) {
        this.videoItemModelList = items;
    }
}
