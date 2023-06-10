package com.example.futurebank;

public class HistoryItem {

    String detail;
    String email;
    String account;
    int image;

    public HistoryItem(String detail, String account,String email, int image) {
        this.detail = detail;
        this.account = account;
        this.email = email;
        this.image = image;
    }

    public String getDetail() {
        return detail;
    }

    public String getAccount() {
        return account;
    }

    public String getEmail() {
        return email;
    }

    public int getImage() {
        return image;
    }
}
