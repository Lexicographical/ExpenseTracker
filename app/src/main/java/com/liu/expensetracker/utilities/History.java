package com.liu.expensetracker.utilities;

import java.util.Set;
import java.util.TreeSet;

public class History {

    TreeSet<HistoryEntry> history = new TreeSet<>();

    public void addEntry(HistoryEntry entry) {
        history.add(entry);
    }

    public Set<HistoryEntry> getEntries() {
        return history;
    }

}
