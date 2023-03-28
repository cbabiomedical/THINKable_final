package com.example.thinkableproject.sample;

public class MusicModelClass {
    private String imageUrl;
    private String songTitle1;
    private String id; //private variables for encapsulation
    private String name;
    private String isFav;
    private String category;

    public MusicModelClass() {
    }

    //Constructor
    public MusicModelClass(String imageUrl, String songTitle1, String id, String name, String isFav) {
        this.imageUrl = imageUrl;
        this.songTitle1 = songTitle1;
        this.id = id;
        this.name = name;
        this.isFav = isFav;
    }

    public MusicModelClass(String imageUrl, String songTitle1, String id, String name, String isFav, String category) {
        this.imageUrl = imageUrl;
        this.songTitle1 = songTitle1;
        this.id = id;
        this.name = name;
        this.isFav = isFav;
        this.category = category;
    }

    public MusicModelClass(String imageUrl, String songTitle1, String id, String name) {
        this.imageUrl = imageUrl;
        this.songTitle1 = songTitle1;
        this.id = id;
        this.name = name;
    }

    public MusicModelClass(String imageUrl, String songTitle1) {
        this.imageUrl = imageUrl;
        this.songTitle1 = songTitle1;
    }

    //public getters and setters to access variables outside class
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSongTitle1() {
        return songTitle1;
    }

    public void setSongTitle1(String songTitle1) {
        this.songTitle1 = songTitle1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsFav() {
        return isFav;
    }

    public void setIsFav(String isFav) {
        this.isFav = isFav;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    //toString
    @Override
    public String toString() {
        return "MusicModelClass{" +
                "imageView=" + imageUrl +
                ", songName='" + songTitle1 + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", isFav='" + isFav + '\'' +
                '}';
    }
}
