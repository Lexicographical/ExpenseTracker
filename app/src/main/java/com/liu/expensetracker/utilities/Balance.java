package com.liu.expensetracker.utilities;

public class Balance implements Comparable<Balance> {

    private String name;
    private double amount;

    public Balance(String name, double amount) {
        this.name = name;
        this.amount = amount;
    }

    public Balance(String name) {
        this.name = name;
        this.amount = 0;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void addAmount(double d) {
        amount += d;
    }

    @Override
    public int compareTo(Balance another) {
        return name.compareTo(another.getName());
    }
}
