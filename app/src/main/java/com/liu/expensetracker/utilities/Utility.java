package com.liu.expensetracker.utilities;

import android.content.Context;

import com.liu.expensetracker.Numpad;

import java.text.DecimalFormat;

public class Utility {

    private static boolean init = false;
    public static Class<?> clazz = Numpad.class;
    public static int MAX_INT_DIGITS = 12;

    public static void init(Context c) {
        if (init) {
            return;
        }

        BalanceManager.init(c);
        ButtonPreferenceManager.init(c);

        init = true;
    }

    public static String formatDouble(double d) {
        DecimalFormat format = new DecimalFormat();
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        String formatted = format.format(d);
        if (formatted.length() > MAX_INT_DIGITS + 3) {
            format = new DecimalFormat("0.00E0");
            formatted = format.format(d);
        }
        return formatted;
    }

}
