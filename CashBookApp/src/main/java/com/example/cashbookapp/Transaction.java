package com.example.cashbookapp;

public class Transaction {
    public String date;
    public String category;
    public String organization;
    public String particulars;
    public double amount;
    public String type; // "C" or "D"

    public Transaction(String date, String category, String organization, String particulars, double amount, String type) {
        this.date = date;
        this.category = category;
        this.organization = organization;
        this.particulars = particulars;
        this.amount = amount;
        this.type = type;
    }

    // Getters for JavaFX
    public String getDate() { return date; }
    public String getCategory() { return category; }
    public String getOrganization() { return organization; }
    public String getParticulars() { return particulars; }
    public double getAmount() { return amount; }
    public String getType() { return type; }
}