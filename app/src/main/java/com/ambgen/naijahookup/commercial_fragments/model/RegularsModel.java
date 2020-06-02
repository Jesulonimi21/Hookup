package com.ambgen.naijahookup.commercial_fragments.model;

public class RegularsModel {
    String name;
    String email;
    String imageurl;
    String userid;

    public RegularsModel(String name, String email, String imageurl, String userid) {
        this.name = name;
        this.email = email;
        this.imageurl = imageurl;
        this.userid = userid;
    }

    public RegularsModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
