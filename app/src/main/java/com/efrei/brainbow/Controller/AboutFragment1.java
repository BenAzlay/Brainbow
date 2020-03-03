package com.efrei.brainbow.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.efrei.brainbow.R;

public class AboutFragment1 extends Fragment {
    private Button fragment2Button;
    private Button backButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_fragment1, container, false);
        backButton = (Button) view.findViewById(R.id.backButton);
        fragment2Button = (Button) view.findViewById(R.id.fragment2Button);

        fragment2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AboutActivity)getActivity()).setNewPager(1);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
