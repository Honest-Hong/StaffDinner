package com.project.boostcamp.staffdinner.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.publiclibrary.inter.ArrayResultListener;
import com.project.boostcamp.staffdinner.R;
import com.project.boostcamp.staffdinner.adapter.StyleRecyclerAdapter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Hong Tae Joon on 2017-08-09.
 */

public class StyleSelectDialog extends DialogFragment implements DataEvent<String>{
    private RecyclerView recyclerView;
    private StyleRecyclerAdapter adapter;
    private ArrayList<String> selectedStyles;
    private ArrayResultListener<String> resultListener;

    public static StyleSelectDialog newInstance(ArrayResultListener<String> resultListener) {
        StyleSelectDialog dialog = new StyleSelectDialog();
        dialog.setResultListener(resultListener);
        return dialog;
    }

    private void setResultListener(ArrayResultListener<String> resultListener) {
        this.resultListener = resultListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_style_select, null);
        setupView(v);
        return new AlertDialog.Builder(context)
                .setTitle("분위기를 선택해주세요")
                .setView(v)
                .setPositiveButton(context.getString(R.string.button_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        resultListener.onResult(selectedStyles);
                    }
                })
                .setNegativeButton(context.getString(R.string.button_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();
    }

    private void setupView(View v) {
        recyclerView = (RecyclerView)v;
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setHasFixedSize(true);
        adapter = new StyleRecyclerAdapter(getContext(), this);
        adapter.setStyles(new ArrayList<String>(Arrays.asList(getContext().getResources().getStringArray(R.array.styles))));
        recyclerView.setAdapter(adapter);
        selectedStyles = new ArrayList<>();
    }

    @Override
    public void onClick(String data) {
        if(selectedStyles.contains(data)) {
            selectedStyles.remove(data);
        } else {
            selectedStyles.add(data);
        }
    }
}
