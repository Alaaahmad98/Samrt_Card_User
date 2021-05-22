package com.example.samrtcarduser.helper;

public class TransactionItem {

    private String uid, cardNumber, price;

    public TransactionItem(String uid, String cardNumber, String price) {
        this.uid = uid;
        this.cardNumber = cardNumber;
        this.price = price;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
