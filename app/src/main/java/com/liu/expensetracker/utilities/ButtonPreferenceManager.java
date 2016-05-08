package com.liu.expensetracker.utilities;

import android.content.Context;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;

public class ButtonPreferenceManager {

    private static File file;
    private static String fn = "buttons.yml";
    private static double[] orig = {1, 2, 5, 10, 20, 50, 100, 200, 500};

    public static void init(Context c) {

        File file = new File(c.getFilesDir(), fn);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Map<String, String> map = read();
        if (map.size() < 9) {
            for (int i = 0; i < 9; i++) {
                map.put("b" + i, String.valueOf(orig));
            }
        }

    }

    public static Map<String, String> read() {

        try {
            YamlReader reader = new YamlReader(new FileReader(file));
            Map<String, String> map = (Map<String, String>) reader.read();
            reader.close();
            return map;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static void save(int button, double val) {

        Map<String, String> map = read();

        DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits(2);
        format.setMaximumIntegerDigits(5);

        map.put("b" + button, format.format(val));

        try {

            YamlWriter writer = new YamlWriter(new FileWriter(file));
            writer.write(map);
            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
