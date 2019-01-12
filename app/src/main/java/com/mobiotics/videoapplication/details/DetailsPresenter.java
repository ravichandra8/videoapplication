package com.mobiotics.videoapplication.details;

import com.mobiotics.videoapplication.Util.ErrorCode;
import com.mobiotics.videoapplication.modal.VideoDataSource.LoadCallBackListener;
import com.mobiotics.videoapplication.modal.VideoRepository;
import com.mobiotics.videoapplication.modal.pojo.Video;
import com.mobiotics.videoapplication.R;
import java.util.List;

public class DetailsPresenter implements DetailsContract.Presenter {

    private DetailsContract.View view;
    private VideoRepository repository;

    public DetailsPresenter(DetailsContract.View view, VideoRepository repository) {
        this.view = view;
        this.repository = repository;
        this.view.setPresenter(this);
    }


    @Override
    public String filterError(Object errorCode) {
        String errorMsg;
        switch ((int) errorCode) {
            case ErrorCode.CONNECTION_PROBLEM:
                errorMsg = view.getErrorString(R.string.connection_problem);
                break;
            case ErrorCode.NETWORK_ERROR:
                errorMsg = view.getErrorString(R.string.network_problem);
                break;

            case ErrorCode.NO_RESULT:
                errorMsg = view.getErrorString(R.string.no_result);
                break;
            default:
                errorMsg = view.getErrorString(R.string.connection_problem);
                break;
        }
        return errorMsg;
    }

    @Override
    public void getParticularContent(String id,boolean isNext) {
        repository.getParticularVideo(id, isNext,new LoadCallBackListener() {
            @Override
            public void onLoaded(Object response) {
                view.getParticularVideo((Video) response);
            }

            @Override
            public void onError(Object error) {
            //ToDO: need to handle error message
                view.showError(filterError(error));
            }
        });
    }

    @Override
    public void getRelatedVideos(String id) {
        repository.getRelatedVideos(id, new LoadCallBackListener() {
            @Override
            public void onLoaded(Object response) {
                view.getRelatedVideos((List<Video>) response);
            }

            @Override
            public void onError(Object error) {
                view.showError(filterError(error));
            }
        });
    }

    @Override
    public void storevideoPosition(String id, long timestamp,boolean videoState) {
        repository.insertVideoPosition(id,timestamp,videoState);
    }

    @Override
    public void deleteVideoPosition(String id) {
     repository.deleteVideoPosition(id);
    }


    @Override
    public void start() {
        view.setUpUI();
    }
}
