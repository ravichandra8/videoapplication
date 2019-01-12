package com.mobiotics.videoapplication.modal;


import com.mobiotics.videoapplication.Util.NetworkStatus;

public interface VideoDataSource {

    void getVideosContent(NetworkStatus networkStats, LoadCallBackListener callBackListener);

    void getParticularVideo(String id,boolean isNext,LoadCallBackListener callBackListener);

    void getRelatedVideos(String id,LoadCallBackListener callBackListener);

    interface LoadCallBackListener {
        void onLoaded(Object response);
        void onError(Object error);
    }
}
