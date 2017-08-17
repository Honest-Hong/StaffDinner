package com.project.boostcamp.publiclibrary.dialog;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Hong Tae Joon on 2017-08-17.
 */

public class ImageDetailFragment extends Fragment {
    public static ImageDetailFragment newInstance(String image) {
        ImageDetailFragment fragment = new ImageDetailFragment();
        Bundle args = new Bundle();
        args.putString(ImageDetailDialog.EXTRA_IMAGE, image);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ImageView imageView = new ImageView(getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        imageView.setLayoutParams(params);
        imageView.setAdjustViewBounds(true);
        Glide.with(this)
                .load(getArguments().getString(ImageDetailDialog.EXTRA_IMAGE))
                .into(imageView);
        return imageView;
    }
}
