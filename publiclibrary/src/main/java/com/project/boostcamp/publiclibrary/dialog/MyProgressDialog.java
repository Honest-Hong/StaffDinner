package com.project.boostcamp.publiclibrary.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
        MyProgressDialog dialog = new MyProgressDialog();
        dialog.show(fragmentManager, null);
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
