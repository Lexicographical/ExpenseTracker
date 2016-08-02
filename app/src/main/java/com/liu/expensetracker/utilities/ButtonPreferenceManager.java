package com.liu.expensetracker.utilities;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

public class ButtonPreferenceManager {

    private static File file;
    private static String fn = "buttons.json";
    private static float[] orig = {1, 2, 5, 10, 20, 50, 100, 200, 500};
    private static boolean clear = false;

    private static JSONObject buttonMap;

    public static void init(Context c) {

        file = new File(c.getFilesDir(), fn);
        if (clear) {
            file.delete();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        buttonMap = FileManager.getData(file);
        try {
            if (buttonMap == null) {
                buttonMap = new JSONObject();
            }

            DecimalFormat format = new DecimalFormat();
            format.setMaximumFractionDigits(0);
            for (int i = 0; i < 9; i++) {
                if (!buttonMap.has("b" + i)) {
                    buttonMap.put("b" + i, format.format(orig[i]));
                }
            }
            FileManager.saveData(file, buttonMap);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void save(int button, double val) {
        try {
            JSONObject json = FileManager.getData(file);
            DecimalFormat format = new DecimalFormat();
            format.setMaximumFractionDigits(2);
            format.setMaximumIntegerDigits(5);
            if (json == null) {
                json = new JSONObject();
            }
            json.put("b" + button, format.format(val));
            FileManager.saveData(file, json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String getButtonText(int i) {
        try {
            return buttonMap.getString("b" + i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}
