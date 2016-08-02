package com.liu.expensetracker.utilities;

public class Balance implements Comparable<Balance> {

    private String name;
    private double amount;
    private History history;

    public Balance(String name, double amount) {
        this.name = name;
        this.amount = amount;
        history = new History();
    }

    public Balance(String name) {
        this(name, 0);
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public History getHistory() {
        return history;
    }

    public void setAmount(double amount) {
        this.amount = amount;
        HistoryEntry entry = new HistoryEntry(amount, HistoryEntry.EntryType.SET, System.currentTimeMillis());
        history.addEntry(entry);
    }

    public void addAmount(double amount) {
        this.amount += amount;
        HistoryEntry entry = new HistoryEntry(amount, HistoryEntry.EntryType.ADD, System.currentTimeMillis());
        history.addEntry(entry);
    }

    @Override
    public int compareTo(Balance another) {
        return name.compareTo(another.getName());
    }

}
