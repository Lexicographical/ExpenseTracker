package com.liu.expensetracker.utilities;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class HistoryManager {

    private static File file;
    private static String fn = "history.json";

    /*

    abc:
      1234567: "SET 53"
      2345897: "ADD 29"
    def:
      542341: "SET 12"
      623145: "ADD 3256"

     */

    public static void init(Context c) {
        try {
            file = new File(c.getFilesDir(), fn);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static History getHistory(Balance balance) {
        try {
            String name = balance.getName();
            JSONObject json = FileManager.getData(file).getJSONObject(name);
            History history = new History();
            Iterator<String> iter = json.keys();
            while (iter.hasNext()) {
                String time = iter.next();
                String text = json.getString(time);
                HistoryEntry entry = HistoryEntry.parseEntry(text, Long.parseLong(time));
                history.addEntry(entry);
            }
            return history;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveHistory() {
        try {
            JSONObject json = new JSONObject();
            for (Balance bal : BalanceManager.getBalanceList()) {
                JSONObject balid = new JSONObject();
                for (HistoryEntry entry : bal.getHistory().getEntries()) {
                    balid.put(String.valueOf(entry.time), entry.type.name() + " " + entry.value);
                }
                json.put(bal.getName(), balid);
            }
            FileManager.saveData(file, json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
