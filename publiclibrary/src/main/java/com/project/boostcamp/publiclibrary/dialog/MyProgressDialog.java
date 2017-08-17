package com.project.boostcamp.publiclibrary.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

/**
 * Created by Hong Tae Joon on 2017-08-14.
 */

public class MyProgressDialog extends DialogFragment {

    public static MyProgressDialog show(FragmentManager fragmentManager) {
        MyProgressDialog dialog = init();
        dialog.show(fragmentManager, null);
        return dialog;
    }

    public static void show(FragmentManager fragmentManager, long delay) {
        final MyProgressDialog dialog = init();
        dialog.show(fragmentManager, null);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, delay);
    }

    private static MyProgressDialog init() {
        MyProgressDialog dialog = new MyProgressDialog();
        dialog.setCancelable(false);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View v = new ProgressBar(getContext());
        return v;
    }
}
