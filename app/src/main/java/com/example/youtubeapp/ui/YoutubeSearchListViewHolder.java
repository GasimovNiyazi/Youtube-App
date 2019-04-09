package com.example.youtubeapp.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.youtubeapp.R;
import com.example.youtubeapp.model.Items;
import com.example.youtubeapp.utilits.OnAdapterItemClickListener;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class YoutubeSearchListViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_youtube_thumbnail)
    YouTubeThumbnailView mThumbnailView;

    @BindView(R.id.text_view_title)
    TextView mTextViewTitle;

    @BindView(R.id.text_view_chanel_title)
    TextView mTextViewChanelTitle;


    public YoutubeSearchListViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void updateUi(List<Items> items, int position, OnAdapterItemClickListener onAdapterItemClickListener){

        Glide.with(itemView.getContext())
                .load(items.get(position).getSnippet().getThumbnails().getDefault().getUrl())
                .into(mThumbnailView);
        mTextViewTitle.setText(items.get(position).getSnippet().getTitle());
        mTextViewChanelTitle.setText(items.get(position).getSnippet().getChannelTitle());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAdapterItemClickListener.onItemClick(position);
            }
        });

    }

}
