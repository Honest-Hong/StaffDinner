package com.project.boostcamp.staffdinner.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.util.ArraySet;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hong Tae Joon on 2017-08-09.
 */

public class StyleSelectDialog extends DialogFragment implements DataEvent<Map.Entry<String, Boolean>>{
    private RecyclerView recyclerView;
    private StyleRecyclerAdapter adapter;
    private HashMap<String, Boolean> styles;
    private ArrayResultListener<String> resultListener;

    public static StyleSelectDialog newInstance(ArrayResultListener<String> resultListener, String currentStyle) {
        StyleSelectDialog dialog = new StyleSelectDialog();
        HashMap<String, Boolean> selectedStyles = new HashMap<>();
        if(!currentStyle.equals("")) {
            currentStyle = currentStyle.replaceAll(" ", "");
            String[] strings = currentStyle.split(",");
            for (String str : strings) {
                selectedStyles.put(str, true);
            }
        }
        dialog.setStyles(selectedStyles);
        dialog.setResultListener(resultListener);
        return dialog;
    }

    public void setStyles(HashMap<String, Boolean> styles) {
        this.styles = styles;
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
                        ArrayList<String> result = new ArrayList<>();
                        for(Map.Entry<String, Boolean> entry: styles.entrySet()) {
                            if(entry.getValue()) {
                                result.add(entry.getKey());
                            }
                        }
                        resultListener.onResult(result);
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
        if(styles == null) {
            styles = new HashMap<>();
        }
        for(String str: new ArrayList<>(Arrays.asList(getContext().getResources().getStringArray(R.array.styles)))) {
            if(!styles.containsKey(str)) {
                styles.put(str, false);
            }
        }
        adapter.setStyles(new ArraySet<>(styles.entrySet()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(Map.Entry<String, Boolean> data) {
        styles.put(data.getKey(), data.getValue());
    }
}
