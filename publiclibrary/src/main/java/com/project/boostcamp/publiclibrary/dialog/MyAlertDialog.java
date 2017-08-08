package com.project.boostcamp.publiclibrary.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.project.boostcamp.publiclibrary.R;
import com.project.boostcamp.publiclibrary.data.ExtraType;

/**
 * Created by Hong Tae Joon on 2017-07-26.
 * 단순한 알람 다이얼로그 클래스
 * 제목과 메시지만 넘겨주면 알림을 띄울 수 있다
 */

public class MyAlertDialog extends DialogFragment {
    private DialogResultListener resultListener;

    public static MyAlertDialog newInstance(String title, String message, DialogResultListener resultListener) {
        MyAlertDialog myAlertDialog = new MyAlertDialog();
        Bundle arg = new Bundle();
        arg.putString(ExtraType.EXTRA_TITLE, title);
        arg.putString(ExtraType.EXTRA_MESSAGE, message);
        myAlertDialog.setArguments(arg);
        myAlertDialog.setResultListener(resultListener);
        return myAlertDialog;
    }

    private void setResultListener(DialogResultListener resultListener) {
        this.resultListener = resultListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString(ExtraType.EXTRA_TITLE);
        String message = getArguments().getString(ExtraType.EXTRA_MESSAGE);
        return new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        resultListener.onPositive();
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton(R.string.button_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        resultListener.onNegative();
                        dialogInterface.dismiss();
                    }
                }).create();
    }
}
