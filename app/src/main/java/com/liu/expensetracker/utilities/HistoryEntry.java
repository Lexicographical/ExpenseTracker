package com.liu.expensetracker.utilities;

public class HistoryEntry implements Comparable<HistoryEntry> {

    public double value;
    public EntryType type;
    public long time;

    @Override
    public int compareTo(HistoryEntry another) {
        if (this.time > another.time) {
            return 1;
        } else if (this.time < another.time) {
            return -1;
        } else {
            return 0;
        }
    }

    enum EntryType {
        SET, ADD;

        public static EntryType getType(String s) {
            for (EntryType type : EntryType.values()) {
                if (type.name().equals(s)) {
                    return type;
                }
            }
            return null;
        }
    }

    public HistoryEntry(double value, EntryType type, long time) {
        this. value = value;
        this.type = type;
        this.time = time;
    }

    public static HistoryEntry parseEntry(String text, long time) {
        String[] tokens = text.split(" ");
        EntryType type = EntryType.getType(tokens[0]);
        double value = Double.parseDouble(tokens[1]);
        HistoryEntry entry = new HistoryEntry(value, type, time);
        return entry;
    }


}
