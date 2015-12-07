package com.charnock.dev.model;


public class Request_Information_Model {
    String id;
    String Site_Name;
    String date;

    public String getSite_Name() {
        return Site_Name;
    }

    public void setSite_Name(String site_Name) {
        Site_Name = site_Name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}