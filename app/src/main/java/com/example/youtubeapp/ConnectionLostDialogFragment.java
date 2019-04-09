package com.example.youtubeapp;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.example.youtubeapp.utilits.Constants;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ConnectionLostDialogFragment extends DialogFragment {

    @BindView(R.id.image_connection_lost)
    ImageView mImageConnectionLost;

    @OnClick(R.id.btn_ok)
    public void clickOk(View view){
        getDialog().dismiss();
    }

    private String mParam1;


    public ConnectionLostDialogFragment() {
        // Required empty public constructor
    }

    public static ConnectionLostDialogFragment newInstance(String param1) {
        ConnectionLostDialogFragment fragment = new ConnectionLostDialogFragment();
        Bundle args = new Bundle();
        args.putString(Constants.ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(Constants.ARG_PARAM1);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_connection_lost_dialog, container, false);
        ButterKnife.bind(this, view);
        Objects.requireNonNull(getDialog().getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
