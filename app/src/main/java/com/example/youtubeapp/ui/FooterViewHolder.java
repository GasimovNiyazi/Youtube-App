package com.example.youtubeapp.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.youtubeapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FooterViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_progress_bar)
    ProgressBar mProgressBar;

    public FooterViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
