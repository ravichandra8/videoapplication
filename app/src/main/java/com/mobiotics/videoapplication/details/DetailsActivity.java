package com.mobiotics.videoapplication.details;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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
import com.mobiotics.videoapplication.modal.VideoRemoteDataSource;
import com.mobiotics.videoapplication.modal.VideoRepository;
import com.mobiotics.videoapplication.modal.MydatabaseAdapter;
import com.mobiotics.videoapplication.modal.pojo.Video;
import com.mobiotics.videoapplication.R;
import java.util.List;

public class DetailsActivity extends AppCompatActivity implements DetailsContract.View{

    DetailsPresenter presenter;
    TextView title,description;
    RecyclerView recyclerView;
    private RelatedVideosAdapter relatedVideosAdapter;

    PlayerView playerView;
    SimpleExoPlayer player;
    private String selectedVideoID;
    LinearLayout rootView;
    private int currentWindow;
    private boolean playWhenReady = false;
    private long playbackPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        selectedVideoID = getIntent().getStringExtra("position");
        presenter = new DetailsPresenter(this, VideoRepository.getInstance(VideoRemoteDataSource.getInstance(),new MydatabaseAdapter(this)));
        presenter.start();

    }

    @Override
    public void showError(String errorMsg) {
        Snackbar.make(rootView, errorMsg, Snackbar.LENGTH_LONG)
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

    @Override
    public String getErrorString(int resourceId) {
        return getResources().getString(resourceId);
    }

    @Override
    public void setUpUI() {
        title=findViewById(R.id.title);
        description=findViewById(R.id.description);
        rootView = findViewById(R.id.root_view);
        recyclerView = findViewById(R.id.video_list);
        playerView = findViewById(R.id.video_view);
    }

    @Override
    public void getParticularVideo(Video video) {
        selectedVideoID = video.getId();

        title.setText(video.getTitle());
        description.setText(video.getDescription());
        if(!video.getUrl().isEmpty()&&player!=null) {
            MediaSource mediaSource = buildMediaSource(Uri.parse(video.getUrl()));
            player.seekTo(currentWindow, video.getVideoDuration());

            player.setPlayWhenReady(playWhenReady);

            playerView.setPlayer(player);
            if(video.getVideoDuration()>0) {
                player.prepare(mediaSource, false, false);
            }
            else{
                player.prepare(mediaSource, true, false);
            }


        }

    }

    @Override
    public void getRelatedVideos(List<Video> videoList) {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        relatedVideosAdapter = new RelatedVideosAdapter(videoList);
        recyclerView.setAdapter(relatedVideosAdapter);
        relatedVideosAdapter.notifyDataSetChanged();
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
            presenter.getParticularContent(selectedVideoID,false);
            presenter.getRelatedVideos(selectedVideoID);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
            presenter.getParticularContent(selectedVideoID,false);
            presenter.getRelatedVideos(selectedVideoID);
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



            player.addListener(new DefaultEventListener() {
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    super.onPlayerStateChanged(playWhenReady, playbackState);

                    if (playWhenReady && playbackState == Player.STATE_ENDED) {
                        Log.d("success", "ended");
                        presenter.deleteVideoPosition(selectedVideoID);
                      presenter.getParticularContent(selectedVideoID,true);
                        presenter.getRelatedVideos(selectedVideoID);
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
            if(playbackPosition>0) {
                presenter.storevideoPosition(selectedVideoID, playbackPosition, playWhenReady);
            }
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
