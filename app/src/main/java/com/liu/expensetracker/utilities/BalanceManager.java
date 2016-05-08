package com.liu.expensetracker.utilities;

import android.content.Context;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class BalanceManager {

    private static File file;
    private static String fn = "balances.yml";

    private static String key1 = "current", key2 = "balances";

    public static void init(Context c) {
        try {
            file = new File(c.getFilesDir(), fn);
            if (!file.exists()) {
                file.createNewFile();
            }
            Map<String, ?> map = read();
            if (map == null || map.size() < 2) {
                Map<String, Object> tree = new LinkedHashMap<>();
                Map<String, String> vals = new LinkedHashMap<>();
                vals.put("default", String.valueOf(0D));
                tree.put(key1, "default");
                tree.put(key2, vals);
                YamlWriter writer = new YamlWriter(new FileWriter(file));
                writer.write(tree);
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void setCurrent(Balance balance) {
        Balance cur = balance;

        Map<String, String> bals = getBalances();
        bals.put(balance.getName(), String.valueOf(balance.getAmount()));

        try {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put(key1, cur.getName());
            map.put(key2, bals);

            YamlWriter writer = new YamlWriter(new FileWriter(file));
            writer.write(map);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Balance getCurrent() {
        Map<String, ?> map = read();
        String name = (String) map.get(key1);
        Balance balance = getBalance(name);
        return balance == null ? new Balance("default", 0) : balance;
    }

    public static Balance getBalance(String s) {
        Map<String, String> bals = getBalances();
        if (bals.containsKey(s)) {
            Balance balance = new Balance(s, Double.parseDouble(bals.get(s)));
            return balance;
        }
        return null;
    }

    public static List<Balance> getBalanceList() {
        Set<Balance> bals = new TreeSet<>(getBalanceSet());
        ArrayList<Balance> bal = new ArrayList<>(bals);
        return bal;
    }

    public static Set<Balance> getBalanceSet() {

        HashSet<Balance> balances = new HashSet<>();
        Map<String, String> values = getBalances();
        for (String s : values.keySet()) {
            balances.add(new Balance(s, Double.parseDouble(values.get(s))));
        }
        return balances;

    }

    @SuppressWarnings("unchecked")
    public static Map<String, String> getBalances() {
        Map<String, ?> map = read();
        Map<String, String> values = (Map<String, String>) map.get(key2);
        return values;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, ?> read() {

        try {
            YamlReader reader = new YamlReader(new FileReader(file));
            Map<String, ?> map = (Map<String, ?>) reader.read();
            reader.close();
            return map;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static void save(Balance balance) {
        String name = balance.getName();
        double amount = balance.getAmount();

        Balance cur = getCurrent();

        Map<String, String> bals = getBalances();
        bals.put(name, String.valueOf(amount));

        try {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put(key1, cur.getName());
            map.put(key2, bals);

            YamlWriter writer = new YamlWriter(new FileWriter(file));
            writer.write(map);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean delete(String name) {
        if (name.equals(key1)) {
            return false;
        }
        Map<String, String> bals = getBalances();
        if (bals.containsKey(name)) {
            bals.remove(name);
            if (getCurrent().getName().equals(name)) {
                setCurrent(getBalance("default"));
            }
            try {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put(key1, getCurrent().getName());
                map.put(key2, bals);

                YamlWriter writer = new YamlWriter(new FileWriter(file));
                writer.write(map);
                writer.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
