package com.mobiotics.videoapplication.homescreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.mobiotics.videoapplication.Util.AppNetworkStatus;
import com.mobiotics.videoapplication.details.DetailsActivity;
import com.mobiotics.videoapplication.modal.ImageRemoteDataSource;
import com.mobiotics.videoapplication.modal.ImageRepository;
import com.mobiotics.videoapplication.modal.pojo.Response;
import java.util.List;
import com.mobiotics.videoapplication.R;

public class MainActivity extends AppCompatActivity implements MainContract.View, MainContract.ImageClickListener, View.OnClickListener {

    private RecyclerView imageRecyclerView;
    private ImageListAdapter imageListAdapter;
    private FrameLayout errorView;
    private Button retryButton;
    private MainPresenter presenter;
    private View rootView;
    private ShimmerFrameLayout shimmerViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new MainPresenter(this, ImageRepository.getInstance(ImageRemoteDataSource.getInstance()));
        presenter.start();
        presenter.getImages(new AppNetworkStatus(this));
    }


    @Override
    public void showLoadingIndicator(boolean active) {
        if (active) {
            shimmerViewContainer.setVisibility(View.VISIBLE);
            shimmerViewContainer.startShimmerAnimation();
        } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    shimmerViewContainer.stopShimmerAnimation();
                    shimmerViewContainer.setVisibility(View.GONE);
                }
            }, 500);

        }
    }

    @Override
    public void showError(String errorMsg) {
        Snackbar.make(rootView, errorMsg, Snackbar.LENGTH_LONG)
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

    @Override
    public void showImages(List<Response> imageLists) {
        if (imageListAdapter == null) {

            LinearLayoutManager manager = new LinearLayoutManager(this);
            imageRecyclerView.setLayoutManager(manager);
            imageListAdapter = new ImageListAdapter(imageLists, this);
            imageListAdapter.setHasStableIds(true);
            imageRecyclerView.setAdapter(imageListAdapter);
        } else {
            imageListAdapter.notifyDataSetChanged();

        }
        imageRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public String getErrorString(int resourceId) {
        return getResources().getString(resourceId);
    }

    @Override
    public void startFullImageActivity(String position) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    @Override
    public void showNetworkError(boolean active) {
        errorView.setVisibility(active ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setUpUI() {
        Toolbar toolbar = findViewById(R.id.tabanim_toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);
        rootView = findViewById(R.id.root_view);
        imageRecyclerView = findViewById(R.id.image_list);
        shimmerViewContainer = findViewById(R.id.shimmer_view_container);
        errorView = findViewById(R.id.retry_layout);
        retryButton = errorView.findViewById(R.id.button_retry);
        retryButton.setOnClickListener(this);
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.presenter = (MainPresenter) presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmerViewContainer.startShimmerAnimation();
    }

    @Override
    protected void onPause() {
        shimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }

    @Override
    public void onImageClick(String position) {

        presenter.showFullImage(position);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_retry) {
            showNetworkError(false);
            presenter.getImages(new AppNetworkStatus(this));
        }
    }
}
