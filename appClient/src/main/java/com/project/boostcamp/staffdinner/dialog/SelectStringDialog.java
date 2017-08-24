package com.project.boostcamp.staffdinner.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.staffdinner.R;
import com.project.boostcamp.staffdinner.adapter.SelectStringAdapter;

/**
 * Created by Hong Tae Joon on 2017-08-17.
 * 문자열 선택 다이얼로그
 */

public class SelectStringDialog extends DialogFragment {
    private String title;
    private String[] data;
    private DataEvent<String> returnEvent;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        SelectStringAdapter adapter = new SelectStringAdapter(getContext(), dataEvent);
        adapter.setData(data);
        View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_select_string, null);
        RecyclerView recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        return new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setView(v)
                .create();
    }

    private final DataEvent<String> dataEvent = new DataEvent<String>() {
        @Override
        public void onClick(String data) {
            dismiss();
            returnEvent.onClick(data);
        }
    };

    private void setTitle(String title) {
        this.title = title;
    }

    private void setData(String[] data) {
        this.data = data;
    }

    private void setReturnEvent(DataEvent<String> returnEvent) {
        this.returnEvent = returnEvent;
    }

    public static class Builder {
        private SelectStringDialog dialog;
        public Builder() {
            dialog = new SelectStringDialog();
        }

        public Builder setTitle(String title) {
            dialog.setTitle(title);
            return this;
        }

        public Builder setData(String[] data) {
            dialog.setData(data);
            return this;
        }

        public Builder setReturnEvent(DataEvent<String> dataEvent) {
            dialog.setReturnEvent(dataEvent);
            return this;
        }

        public SelectStringDialog create() {
            return dialog;
        }
    }
}
