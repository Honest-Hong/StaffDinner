package com.project.boostcamp.publiclibrary.util;

import android.widget.EditText;

/**
 * Created by Hong Tae Joon on 2017-08-01.
 */

public class EditTextHelper {
    public static boolean greaterLength(EditText e, int length) {
        return e.getText().toString().length() >= length;
    }
}
