package com.example.ump_e_wallet_project;

public class Payment {

    public String getAmount() {
        return amount;
    }

    public String getPaymentdetail() {
        return paymentdetail;
    }

    private String amount;

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setPaymentdetail(String paymentdetail) {
        this.paymentdetail = paymentdetail;
    }

    private String paymentdetail;

}
