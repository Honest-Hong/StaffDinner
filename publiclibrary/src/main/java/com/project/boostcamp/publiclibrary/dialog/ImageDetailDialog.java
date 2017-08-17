package com.project.boostcamp.publiclibrary.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

/**
 * Created by Hong Tae Joon on 2017-08-17.
 */

public class ImageDetailDialog extends DialogFragment {
    public static final int MODE_LOCAL = 0x1;
    public static final int MODE_INTERNET = 0x2;
    public static final String EXTRA_IMAGE = "image";
    private String title;
    private int selected;
    private int mode;
    private ArrayList<String> images;

    public static class Builder {
        private Context context;
        private ImageDetailDialog dialog;

        public Builder(Context context) {
            this.context = context;
            dialog = new ImageDetailDialog();
        }

        public Builder setTitle(String title) {
            dialog.setTitle(title);
            return this;
        }

        public Builder setImages(ArrayList<String> images) {
            dialog.setImages(images);
            return this;
        }

        public Builder useLocal() {
            dialog.setMode(MODE_LOCAL);
            return this;
        }

        public Builder useInternet() {
            dialog.setMode(MODE_INTERNET);
            return this;
        }

        public Builder setSelected(int selected) {
            dialog.setSelected(selected);
            return this;
        }

        public ImageDetailDialog create() {
            return dialog;
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(windowParams);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ViewPager viewPager = new ViewPager(getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        viewPager.setLayoutParams(params);
        viewPager.setId(View.generateViewId());
        ImageDetailAdapter adapter = new ImageDetailAdapter(getChildFragmentManager(), images);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(selected);
        return viewPager;
    }
}
