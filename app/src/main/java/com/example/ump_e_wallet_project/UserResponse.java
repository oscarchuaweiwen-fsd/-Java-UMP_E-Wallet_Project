package com.example.ump_e_wallet_project;

import java.util.ArrayList;

public class UserResponse {

    private ArrayList<Card> card;

    public ArrayList<Card> getCard() {
        return card;
    }

    public void setCard(ArrayList<Card> card) {
        this.card = card;
    }

    private ArrayList<Payment> payment;

    public ArrayList<Payment> getPayment(){
        return payment;
    }

    public void setPayment(ArrayList<Payment> payment){
        this.payment = payment;
    }

    // others
    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    private String balance;
    private String email;
    private String name;
    private String phonenumber;


}