package com.example.youtubeapp.utilits;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public abstract class PaginationScrolListener extends RecyclerView.OnScrollListener {

    private LinearLayoutManager mLayoutManager;

    public PaginationScrolListener(LinearLayoutManager layoutManager) {
        mLayoutManager = layoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = mLayoutManager.getChildCount();
        int totalItemCount = mLayoutManager.getItemCount();
        int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

        if (!isLastPage() && !isLoading()) {
            if (firstVisibleItemPosition+visibleItemCount>=totalItemCount
                    &&firstVisibleItemPosition>0
                    &&totalItemCount<getTotalPageCount()){
                loadMoreItems();
            }


        }


    }

    public abstract void loadMoreItems();

    public abstract int getTotalPageCount();

    public abstract boolean isLastPage();

    public abstract boolean isLoading();
}
