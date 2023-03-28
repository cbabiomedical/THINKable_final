package com.example.thinkableproject.sample;

public class DownloadMusicModelClass {
    private String item_title;
    private String key_id;
    private String item_image;  //private variables for encapsulation
    private String url;

    public DownloadMusicModelClass() {
    }

    //Constructor
    public DownloadMusicModelClass(String item_title, String key_id, String item_image, String url) {
        this.item_title = item_title;
        this.key_id = key_id;
        this.item_image = item_image;
        this.url = url;
    }

    public DownloadMusicModelClass(String item_title, String item_image) {
        this.item_title = item_title;
        this.item_image = item_image;
    }
    //public getters and setters to access variables outside class
    public String getItem_title() {
        return item_title;
    }

    public void setItem_title(String item_title) {
        this.item_title = item_title;
    }

    public String getKey_id() {
        return key_id;
    }

    public void setKey_id(String key_id) {
        this.key_id = key_id;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    //toString method
    @Override
    public String toString() {
        return "DownloadMusicModelClass{" +
                "item_title='" + item_title + '\'' +
                ", key_id='" + key_id + '\'' +
                ", item_image='" + item_image + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
