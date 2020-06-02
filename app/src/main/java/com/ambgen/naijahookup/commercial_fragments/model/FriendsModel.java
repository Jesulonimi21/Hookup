package com.ambgen.naijahookup.commercial_fragments.model;

public class FriendsModel {
    String user_id;
    String name;
    boolean messageexists;
    long invertedtimestamp;

    public boolean isMessageexists() {
        return messageexists;
    }

    public void setMessageexists(boolean messageexists) {
        this.messageexists = messageexists;
    }

    public long getinvertedtimestamp() {
        return invertedtimestamp;
    }

    public void setinvertedtimestamp(long invertedtimestamp) {
        this.invertedtimestamp = invertedtimestamp;
    }

    public FriendsModel() {
    }

    public FriendsModel(String user_id, String name, long invertedtimestamp, boolean messageexists) {
        this.user_id = user_id;
        this.name = name;
        this.invertedtimestamp = invertedtimestamp;
        this.messageexists=messageexists;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
