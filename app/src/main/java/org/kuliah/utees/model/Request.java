package org.kuliah.utees.model;

import java.io.Serializable;

public class Request implements Serializable {

    private String nama;
    private String email;
    private String telepon;
    private String mImageUrl;

    private String key;

    public Request(){

    }

    public Request(String nama, String email, String telepon, String imageUrl){
        this.nama = nama;
        this.email = email;
        this.telepon = telepon;
        this.mImageUrl = imageUrl;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public String getImageUrl() {
        return mImageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.mImageUrl = imageUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String toString(){
        return " "+nama+"\n" +
                " "+email+"\n" +
                " "+telepon+"\n" +
                " "+mImageUrl;
    }
}
