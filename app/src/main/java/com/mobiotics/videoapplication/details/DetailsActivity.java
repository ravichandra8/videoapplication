package com.mobiotics.videoapplication.details;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Player.DefaultEventListener;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.mobiotics.videoapplication.details.DetailsContract.Presenter;
import com.mobiotics.videoapplication.modal.ImageRemoteDataSource;
import com.mobiotics.videoapplication.modal.ImageRepository;
import com.mobiotics.videoapplication.modal.pojo.Response;
import com.mobiotics.videoapplication.R;
import java.util.List;

public class DetailsActivity extends AppCompatActivity implements DetailsContract.View{

    DetailsPresenter presenter;
    TextView title,description;
    RecyclerView imageRecyclerView;
    private ImageListAdapter imageListAdapter;

    PlayerView playerView;
    SimpleExoPlayer player;
    private String selectedPosition;

    private int currentWindow;
    private boolean playWhenReady = false;
    private long playbackPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);


         selectedPosition= getIntent().getStringExtra("position");
        presenter = new DetailsPresenter(this, ImageRepository.getInstance(ImageRemoteDataSource.getInstance()));
        presenter.start();






    }

    @Override
    public void showLoadingIndicator(boolean active) {

    }

    @Override
    public void showError(String errorMsg) {

    }

    @Override
    public String getErrorString(int resourceId) {
        return null;
    }

    @Override
    public void setUpUI() {
        title=(TextView)findViewById(R.id.title);
        description=(TextView)findViewById(R.id.description);
        imageRecyclerView = findViewById(R.id.image_list);
        playerView = findViewById(R.id.video_view);
    }

    @Override
    public void getParticularVideo(Response response) {
        Log.d("ravi",response.getId());

        title.setText(response.getTitle());
        description.setText(response.getDescription());
        if(!response.getUrl().isEmpty()) {
            MediaSource mediaSource = buildMediaSource(Uri.parse(response.getUrl()));

            player.prepare(mediaSource, true, false);
        }

    }

    @Override
    public void getRemainingContent(List<Response> responses) {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        imageRecyclerView.setLayoutManager(manager);
        imageListAdapter = new ImageListAdapter(responses);
        imageRecyclerView.setAdapter(imageListAdapter);
        imageListAdapter.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(Presenter presenter) {
      this.presenter= (DetailsPresenter) presenter;
    }



    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();

            presenter.getParticularContent(selectedPosition);
            presenter.getRemainingContent(selectedPosition);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();

            presenter.getParticularContent(selectedPosition);
            presenter.getRemainingContent(selectedPosition);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void initializePlayer() {
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());

            player.seekTo(currentWindow, playbackPosition);

            player.setPlayWhenReady(playWhenReady);

            playerView.setPlayer(player);

            player.addListener(new DefaultEventListener() {
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    super.onPlayerStateChanged(playWhenReady, playbackState);

                    if (playWhenReady && playbackState == Player.STATE_ENDED) {
                        Log.d("success", "ended");

                      presenter.getParticularContent(selectedPosition+1);
                      presenter.getRemainingContent(selectedPosition+1);

                    }
                }
            });

        }



    }


    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            Log.d("position", player.getCurrentPosition() + "");
            player = null;
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
            new DefaultHttpDataSourceFactory("exoplayer-codelab"))
            .createMediaSource(uri);
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

}
