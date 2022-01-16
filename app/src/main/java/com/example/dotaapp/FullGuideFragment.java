package com.example.dotaapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class FullGuideFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.full_guide, container, false);

        Bundle bundle = getArguments();
        Guide g = (Guide) bundle.getParcelable("guide");

        TextView fullGuideTitle = (TextView) view.findViewById(R.id.fullGuideTitle);
        TextView fullGuideString = (TextView) view.findViewById(R.id.fullGuideString);
        TextView fullGuideUsername = (TextView) view.findViewById(R.id.fullGuideUsername);
        TextView fullGuideLocation = (TextView) view.findViewById(R.id.fullGuideLocation);

        fullGuideTitle.setText(g.getTitle());
        fullGuideString.setText(g.getString());
        fullGuideUsername.setText("By: " + g.getUsername());
        fullGuideLocation.setText("From: " + g.getLocation());
        fullGuideString.setMovementMethod(new ScrollingMovementMethod());

        return view;
    }
}
