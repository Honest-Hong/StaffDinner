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

import com.project.boostcamp.publiclibrary.data.StyleListData;
import com.project.boostcamp.publiclibrary.inter.ArrayResultListener;
import com.project.boostcamp.publiclibrary.inter.ViewHolderEvent;
import com.project.boostcamp.staffdinner.R;
import com.project.boostcamp.staffdinner.adapter.StyleRecyclerAdapter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Hong Tae Joon on 2017-08-09.
 * 분위기 선택 다이얼로그
 */

public class StyleSelectDialog extends DialogFragment implements ViewHolderEvent<StyleListData> {
    private StyleRecyclerAdapter adapter;
    private ArrayList<StyleListData> data;
    private String selectedStyles;
    private ArrayResultListener<String> resultListener;

    public static StyleSelectDialog newInstance(ArrayResultListener<String> resultListener, String currentStyle) {
        StyleSelectDialog dialog = new StyleSelectDialog();
        dialog.setSelectedStyles(currentStyle);
        dialog.setResultListener(resultListener);
        return dialog;
    }

    private void setSelectedStyles(String selectedStyles) {
        this.selectedStyles = selectedStyles;
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
                .setTitle(R.string.please_select_style)
                .setView(v)
                .setPositiveButton(context.getString(R.string.complete_select), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ArrayList<String> result = new ArrayList<>();
                        for(StyleListData style: data) {
                            if(style.isChecked()) {
                                result.add(style.getName());
                            }
                        }
                        resultListener.onResult(result);
                    }
                })
                .setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();
    }

    private void setupView(View v) {
        RecyclerView recyclerView = (RecyclerView)v;
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setHasFixedSize(true);
        adapter = new StyleRecyclerAdapter(getContext(), this);
        data = new ArrayList<>();
        for(String str: new ArrayList<>(Arrays.asList(getContext().getResources().getStringArray(R.array.styles)))) {
            data.add(new StyleListData(str, false));
        }
        if(!selectedStyles.equals("")) {
            selectedStyles = selectedStyles.replaceAll(" ", "");
            String[] strings = selectedStyles.split(",");
            for (String str : strings) {
                for(int i=0; i<data.size(); i++) {
                    if(data.get(i).getName().equals(str)) {
                        data.get(i).setChecked(true);
                        break;
                    }
                }
            }
        }
        adapter.setStyles(data);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(StyleListData data, int position) {
        this.data.get(position).setChecked(!data.isChecked());
        adapter.notifyItemChanged(position);
    }
}
