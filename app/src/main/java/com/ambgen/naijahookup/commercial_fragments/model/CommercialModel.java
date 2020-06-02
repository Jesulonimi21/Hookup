package com.ambgen.naijahookup.commercial_fragments.model;

public class CommercialModel {
    String name;
    String email;
    String imageurl;
    String nationalid;
    String password;
    String userid;

    public CommercialModel(String name, String email, String imageurl, String nationalid, String password, String userid) {
        this.name = name;
        this.email = email;
        this.imageurl = imageurl;
        this.nationalid = nationalid;
        this.password = password;
        this.userid = userid;
    }

    public CommercialModel() {
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

    public String getNationalid() {
        return nationalid;
    }

    public void setNationalid(String nationalid) {
        this.nationalid = nationalid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
