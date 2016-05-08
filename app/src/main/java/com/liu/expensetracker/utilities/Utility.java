package com.liu.expensetracker.utilities;

import android.content.Context;

import com.liu.expensetracker.Numpad;

public class Utility {

    private static boolean init = false;
    public static Class<?> clazz = Numpad.class;

    public static void init(Context c) {
        if (init) {
            return;
        }

        BalanceManager.init(c);
        ButtonPreferenceManager.init(c);

        init = true;
    }

}
