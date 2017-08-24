package com.project.boostcamp.staffdinner.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.project.boostcamp.publiclibrary.data.ExtraType;
import com.project.boostcamp.publiclibrary.inter.ReviewListener;
import com.project.boostcamp.staffdinner.R;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by Hong Tae Joon on 2017-08-11.
 * 리뷰 작성 다이얼로그
 */

public class WriteReviewDialog extends DialogFragment implements MaterialRatingBar.OnRatingChangeListener{
    private EditText editContent;
    private MaterialRatingBar ratingBar;
    private ReviewListener reviewListener;
    private TextView textRating;

    public static WriteReviewDialog newInstance(String reciever, String writer, ReviewListener reviewListener) {
        WriteReviewDialog dialog = new WriteReviewDialog();
        dialog.setReviewListener(reviewListener);
        Bundle args = new Bundle();
        args.putString(ExtraType.EXTRA_RECEIVER, reciever);
        args.putString(ExtraType.EXTRA_WRITER, writer);
        dialog.setArguments(args);
        return dialog;
    }

    private void setReviewListener(ReviewListener reviewListener) {
        this.reviewListener = reviewListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_write_review, null);
        editContent = v.findViewById(R.id.edit_content);
        textRating = v.findViewById(R.id.text_rating);
        ratingBar = v.findViewById(R.id.rating_bar);
        ratingBar.setOnRatingChangeListener(this);
        ratingBar.setRating(2.5f);
        TextView textWriter = v.findViewById(R.id.text_writer);
        textWriter.setText(getArguments().getString(ExtraType.EXTRA_WRITER));
        TextView textReceiver = v.findViewById(R.id.text_receiver);
        textReceiver.setText(getArguments().getString(ExtraType.EXTRA_RECEIVER));
        return new AlertDialog.Builder(getContext())
                .setTitle("리뷰 작성")
                .setPositiveButton(getString(R.string.send_review), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        reviewListener.onReview(editContent.getText().toString(), ratingBar.getProgress());
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setView(v)
                .create();
    }

    @Override
    public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
        textRating.setText(getString(R.string.rating_format, rating));
    }
}
