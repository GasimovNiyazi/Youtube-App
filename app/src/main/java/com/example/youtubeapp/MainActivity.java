package com.example.youtubeapp;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.SearchView;
import android.widget.Toast;


import com.example.youtubeapp.api.YoutubeSearchApi;
import com.example.youtubeapp.api.YoutubeSearchService;
import com.example.youtubeapp.model.Items;
import com.example.youtubeapp.model.YoutubeSearchResult;
import com.example.youtubeapp.utilits.OnAdapterItemClickListener;
import com.example.youtubeapp.ui.YoutubeSearchListAdapter;
import com.example.youtubeapp.utilits.Config;
import com.example.youtubeapp.utilits.Constants;
import com.example.youtubeapp.utilits.PaginationScrolListener;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "MainActivity";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout mShimmerFrameLayout;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;


    private YoutubeSearchApi mYoutubeSearchApi;
    private Retrofit mRetrofit;
    private LinearLayoutManager mLayoutManager;
    private YoutubeSearchListAdapter mAdapter;
    private Handler mHandler;
    private SearchView mSearchView;


    private String mSearchKey = "";
    private String mChart = "mostPopular";
    private String mPageToken = "";

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int mTotalPage = 5;
    private int mCurrentPage = Constants.PAGE_START;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        if (!isOnline(this)) {
            mShimmerFrameLayout.stopShimmerAnimation();
            mShimmerFrameLayout.setVisibility(View.GONE);
            showConnectionDialog();
        }

        mRetrofit = YoutubeSearchService.getRetrofitInstance();
        mYoutubeSearchApi = mRetrofit.create(YoutubeSearchApi.class);
        getSearchApiResult();
        mRefreshLayout.setOnRefreshListener(this);
        mHandler = new Handler();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isOnline(MainActivity.this)) mShimmerFrameLayout.startShimmerAnimation();
    }

    @Override
    protected void onPause() {
        mShimmerFrameLayout.stopShimmerAnimation();
        super.onPause();
    }

    @Override
    public void onRefresh() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getSearchApiResult();
                mPageToken = "";
                mRefreshLayout.setRefreshing(false);
            }
        }, 1000);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searrch, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_search);
        mSearchView = (SearchView) menuItem.getActionView();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_search:
                mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        updateSearchQueries(query, "");
                        getSearchApiResult();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        mHandler.removeCallbacks(runnable);
                        if (!newText.equals("")) {
                            updateSearchQueries(newText, "");
                            mHandler.postDelayed(runnable, 1000);
                        } else {
                            updateSearchQueries("", "mostPopular");
                            getSearchApiResult();
                        }


                        return true;
                    }
                });
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getSearchApiResult() {


        getSearchResultCall().enqueue(new Callback<YoutubeSearchResult>() {
            @Override
            public void onResponse(@NonNull Call<YoutubeSearchResult> call, @NonNull Response<YoutubeSearchResult> response) {
                if (!response.isSuccessful()) {
                    Log.v(TAG, getString(R.string.msg_response_code) + response.code());
                    switch (response.code()) {
                        case 403:
                            Toast.makeText(MainActivity.this, R.string.msg_response_code_403, Toast.LENGTH_LONG).show();
                            mShimmerFrameLayout.stopShimmerAnimation();
                            return;
                    }
                }
                YoutubeSearchResult result = response.body();
                if (result == null) {
                    Toast.makeText(MainActivity.this, R.string.msg_data_downloading_break, Toast.LENGTH_LONG).show();
                    return;
                }
                List<Items> itemsList = result.getItems();
                mTotalPage = (result.getPageInfo().getTotalresults())
                        / (result.getPageInfo().getResultsPerPage());
                mPageToken = result.getNextPageToken();
                mShimmerFrameLayout.stopShimmerAnimation();
                mShimmerFrameLayout.setVisibility(View.GONE);
                initRecyclerView(itemsList);

                if (mCurrentPage <= mTotalPage) mAdapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(@NonNull Call<YoutubeSearchResult> call, @NonNull Throwable t) {

                Log.v(TAG, t.getMessage());

            }
        });
    }

    private void initRecyclerView(List<Items> itemsList) {

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new YoutubeSearchListAdapter(this, itemsList, new OnAdapterItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getApplicationContext(), DialogActivity.class);
                intent.putExtra(Constants.EXTRA_VIDEO_ID_NAME, itemsList.get(position).getId().getVideoId());
                startActivity(intent);
            }
        });
        mAdapter.clear();
        mRecyclerView.setAdapter(mAdapter);
        callScrollListener();
    }

    private void callScrollListener() {
        mRecyclerView.addOnScrollListener(new PaginationScrolListener(mLayoutManager) {
            @Override
            public void loadMoreItems() {
                isLoading = true;
                mCurrentPage++;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);

            }

            @Override
            public int getTotalPageCount() {
                return mTotalPage;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

    }

    private void loadNextPage() {

        getSearchResultCall().enqueue(new Callback<YoutubeSearchResult>() {
            @Override
            public void onResponse(@NonNull Call<YoutubeSearchResult> call, @NonNull Response<YoutubeSearchResult> response) {

                mAdapter.removeLoadingFooter();
                isLoading = false;

                YoutubeSearchResult results = response.body();
                if (results == null) return;
                List<Items> itemsList = results.getItems();
                mPageToken = results.getNextPageToken();
                mAdapter.addAll(itemsList);

                if (mCurrentPage != mTotalPage) mAdapter.addLoadingFooter();
                else isLastPage = true;

            }

            @Override
            public void onFailure(@NonNull Call<YoutubeSearchResult> call, @NonNull Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private Call<YoutubeSearchResult> getSearchResultCall() {
        return mYoutubeSearchApi.getYoutubeSearchResult(Constants.PART, Constants.MAX_RESULT,
                mChart, mSearchKey, Constants.REGION_CODE, Constants.TYPE, mPageToken, Config.YOUTUBE_API_KEY);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getSearchApiResult();
            mSearchView.clearFocus();
        }
    };

    private void showConnectionDialog() {
        FragmentManager manager = getSupportFragmentManager();
        ConnectionLostDialogFragment connectionLostDialogFragment = ConnectionLostDialogFragment.newInstance(Constants.ARG_PARAM1);
        connectionLostDialogFragment.show(manager, Constants.DIALOG_TAG);
    }

    private boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm != null ? cm.getActiveNetworkInfo() : null;
        return info != null && info.isConnected();
    }

    private void updateSearchQueries(String searchKey, String chart) {
        mSearchKey = searchKey;
        mChart = "";
        mPageToken = "";
    }

}
