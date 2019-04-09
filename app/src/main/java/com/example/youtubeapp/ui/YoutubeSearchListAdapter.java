package com.example.youtubeapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.youtubeapp.R;
import com.example.youtubeapp.model.Items;
import com.example.youtubeapp.utilits.OnAdapterItemClickListener;

import java.util.List;

import static com.example.youtubeapp.utilits.Constants.VIEW_TYPE_ITEM;
import static com.example.youtubeapp.utilits.Constants.VIEW_TYPE_LOADING;

public class YoutubeSearchListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private boolean isLoadingVisible = false;


    private Context mContext;
    private List<Items> mItemsList;
    private OnAdapterItemClickListener mItemClickListener;

    public YoutubeSearchListAdapter(Context context, List<Items> itemsList, OnAdapterItemClickListener onAdapterItemClickListener) {
        mContext = context;
        mItemsList = itemsList;
        mItemClickListener = onAdapterItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (mItemsList.size() - 1 == position && isLoadingVisible) return VIEW_TYPE_LOADING;
        return VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view;
        switch (i) {
            case VIEW_TYPE_ITEM:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_youtube_video_list, viewGroup, false);
                return new YoutubeSearchListViewHolder(view);
            case VIEW_TYPE_LOADING:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_footer, viewGroup, false);
                return new FooterViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


        switch (getItemViewType(i)) {

            case VIEW_TYPE_ITEM:
                YoutubeSearchListViewHolder holder = (YoutubeSearchListViewHolder) viewHolder;
                holder.updateUi(mItemsList, i, mItemClickListener);
                break;
            case VIEW_TYPE_LOADING:
                break;

        }

    }

    @Override
    public int getItemCount() {
        return mItemsList == null ? 0 : mItemsList.size();
    }

    public void add(Items items) {
        mItemsList.add(items);
        notifyItemInserted(mItemsList.size() - 1);
    }

    public void addAll(List<Items> itemsList) {
        for (Items items : itemsList) {
            add(items);
        }

    }

    public void remove(Items items) {

        int position = mItemsList.indexOf(items);
        if (position > -1) {
            mItemsList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingVisible = false;
        if (getItemCount() > 0) {
            remove(mItemsList.get(0));
        }
    }

    public void addLoadingFooter() {
        isLoadingVisible = true;
        add(new Items());
    }

    public void removeLoadingFooter() {

        isLoadingVisible = false;
        int position = mItemsList.size() - 1;
        Items items = mItemsList.get(position);
        if (items != null) {
            mItemsList.remove(position);
            notifyItemRemoved(position);
        }

    }


}
