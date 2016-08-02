package com.liu.expensetracker.utilities;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BalanceManager {

    private static File file;
    private static String fn = "balances.json";
    private static String key1 = "current", key2 = "balances";
    private static List<Balance> balances = new ArrayList<>();
    private static Balance current;

    /*
    current: something
    balances:
      abc: 123
      def: 456

     */
    public static void init(Context c) {
        try {
            file = new File(c.getFilesDir(), fn);
            if (!file.exists()) {
                file.createNewFile();
            }
            try {
                JSONObject json = FileManager.getData(file);
                if (json == null || !json.has(key1) || !json.has(key2)) {
                    json = new JSONObject();
                    json.put(key1, "default");
                    JSONObject bals = new JSONObject();
                    bals.put("default", 0D);
                    json.put(key2, bals);
                    FileManager.saveData(file, json);
                }
                JSONObject bals = json.getJSONObject(key2);
                Iterator<String> iter = bals.keys();
                while (iter.hasNext()) {
                    String name = iter.next();
                    double value = json.getDouble(name);
                    Balance balance = new Balance(name, value);
                    balances.add(balance);
                }
                String name = json.getString(key1);
                current = getBalance(name);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static List<Balance> getBalanceList() {
        return balances;
    }

    public static Balance getCurrent() {
        try {
            JSONObject json = FileManager.getData(file);
            String name = json.getString(key1);
            Balance balance = getBalance(name);
            return balance;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Balance getBalance(String name) {
        for (Balance b : getBalanceList()) {
            if (b.getName().equals(name)) {
                return b;
            }
        }
        return null;
    }

    public static void saveBalance(Balance balance) {
        boolean match = false;
        for (Balance b : balances) {
            if (b.getName().equals(balance)) {
                b.setAmount(balance.getAmount());
                match = true;
            }
        }
        if (!match) {
            balances.add(balance);
        }
        saveData();
    }

    public static void saveData() {
        try {
            JSONObject json = new JSONObject();
            json.put(key1, current.getName());
            JSONObject bals = new JSONObject();
            for (Balance b : balances) {
                bals.put(b.getName(), b.getAmount());
            }
            json.put(key2, bals);
            FileManager.saveData(file, json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void setCurrentBalance(Balance balance) {
        current = balance;
        saveData();
    }

    public static boolean deleteBalance(String name) {
        int ind = -1;
        for (int i = 0; i < balances.size(); i++) {
            if (balances.get(i).getName().equals(name)) {
                ind = i;
            }
        }
        if (ind >= 0) {
            balances.remove(ind);
            return true;
        }
        return false;

    }

}
