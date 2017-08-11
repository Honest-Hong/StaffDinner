package com.project.boostcamp.staffdinner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.boostcamp.publiclibrary.api.RetrofitClient;
import com.project.boostcamp.publiclibrary.data.ExtraType;
import com.project.boostcamp.publiclibrary.domain.EventDTO;
import com.project.boostcamp.staffdinner.GlideApp;
import com.project.boostcamp.staffdinner.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hong Tae Joon on 2017-08-11.
 */

public class EventFragment extends Fragment {
    @BindView(R.id.image_view) ImageView imageView;
    @BindView(R.id.text_title) TextView textTitle;
    @BindView(R.id.text_content) TextView textContent;

    public static EventFragment newInstance(EventDTO eventDTO) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        String imageUrl = RetrofitClient.getInstance().getEventImageUrl(eventDTO.getNumber());
        args.putString(ExtraType.EXTRA_IMAGE, imageUrl);
        args.putString(ExtraType.EXTRA_TITLE, eventDTO.getTitle());
        args.putString(ExtraType.EXTRA_CONTENT, eventDTO.getContent());
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event, container, false);
        ButterKnife.bind(this, v);
        Bundle args = getArguments();
        GlideApp.with(v)
                .load(args.getString(ExtraType.EXTRA_IMAGE))
                .centerCrop()
                .into(imageView);
        textTitle.setText(args.getString(ExtraType.EXTRA_TITLE));
        textContent.setText(args.getString(ExtraType.EXTRA_CONTENT));
        return v;
    }
}
