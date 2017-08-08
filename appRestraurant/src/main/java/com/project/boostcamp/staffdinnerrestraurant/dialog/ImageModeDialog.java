package com.project.boostcamp.staffdinnerrestraurant.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.boostcamp.staffdinnerrestraurant.R;

/**
 * Created by Hong Tae Joon on 2017-08-08.
 */

public class ImageModeDialog extends DialogFragment implements View.OnClickListener{
    public static final int MODE_CAMERA = 0x0;
    public static final int MODE_GALARY = 0x1;
    private ImageSelector imageSelector;

    public static ImageModeDialog newInstace(ImageSelector imageSelector) {
        ImageModeDialog imageModeDialog = new ImageModeDialog();
        imageModeDialog.setImageSelector(imageSelector);
        return imageModeDialog;
    }

    public void setImageSelector(ImageSelector imageSelector) {
        this.imageSelector = imageSelector;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_image_mode, container, false);
        v.findViewById(R.id.button_camera).setOnClickListener(this);
        v.findViewById(R.id.button_galary).setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.button_camera:
                imageSelector.onSelect(MODE_CAMERA);
                break;
            case R.id.button_galary:
                imageSelector.onSelect(MODE_GALARY);
                break;
        }
        dismiss();
    }

    public interface ImageSelector {
        void onSelect(int mode);
    }
}
