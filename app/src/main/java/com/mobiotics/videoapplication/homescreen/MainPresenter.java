package com.mobiotics.videoapplication.homescreen;

import com.mobiotics.videoapplication.Util.ErrorCode;
import com.mobiotics.videoapplication.Util.NetworkStatus;
import com.mobiotics.videoapplication.modal.VideoDataSource.LoadCallBackListener;
import com.mobiotics.videoapplication.modal.VideoRepository;
import com.mobiotics.videoapplication.modal.pojo.Video;
import  com.mobiotics.videoapplication.R;
import java.util.List;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View view;
    private VideoRepository repository;

    public MainPresenter(MainContract.View view, VideoRepository repository) {
        this.view = view;
        this.repository = repository;
        this.view.setPresenter(this);
    }

    @Override
    public void getListOfVideos(NetworkStatus networkStatus) {
        view.showLoadingIndicator(true);
        repository.getVideosContent(networkStatus, new LoadCallBackListener() {
            @Override
            public void onLoaded(Object response) {
                view.showLoadingIndicator(false);
                view.showListOfVideos((List<Video>) response);
            }

            @Override
            public void onError(Object error) {
                view.showLoadingIndicator(false);
                if ((int) error == ErrorCode.NETWORK_ERROR) {
                    view.showNetworkError(true);
                } else {
                    view.showError(filterError(error));
                }
            }
        });
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
    public void navigateToDetailsPage(String position) {
        view.navigateToDetailsPage(position);
    }

    @Override
    public void start() {
        view.setUpUI();
    }
}
