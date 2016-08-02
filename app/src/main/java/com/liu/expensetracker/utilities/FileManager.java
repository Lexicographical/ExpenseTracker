package com.liu.expensetracker.utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {

    public static JSONObject getData(File file) {
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            for (String s = reader.readLine(); s != null; s = reader.readLine()) {
                sb.append(s);
            }
            reader.close();
            return new JSONObject(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveData(File file, JSONObject json) {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(json.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
