package com.example.Subcept.model;

public class survey_Data {

    String title;
    String desc;
    Integer ImageURL;

    public survey_Data(String title, String desc, Integer ImageURL){
        this.title = title;
        this.desc = desc;
        this.ImageURL = ImageURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getImageURL() {
        return ImageURL;
    }

    public void setImageURL(Integer imageURL) {
        ImageURL = imageURL;
    }
}
