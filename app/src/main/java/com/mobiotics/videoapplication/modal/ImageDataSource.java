package com.mobiotics.videoapplication.modal;


import com.mobiotics.videoapplication.Util.NetworkStatus;

public interface ImageDataSource {

    void getVideosContent(NetworkStatus networkStats, LoadCallBackListener callBackListener);

    void getParticularVideo(String id,LoadCallBackListener callBackListener);

    void getRemainingContent(String id,LoadCallBackListener callBackListener);

    interface LoadCallBackListener {
        void onLoaded(Object response);

        void onError(Object error);
    }
}
