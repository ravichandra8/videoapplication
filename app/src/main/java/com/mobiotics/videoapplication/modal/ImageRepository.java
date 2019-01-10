package com.mobiotics.videoapplication.modal;

import com.mobiotics.videoapplication.Util.ErrorCode;
import com.mobiotics.videoapplication.Util.NetworkStatus;
import com.mobiotics.videoapplication.modal.pojo.Response;
import java.util.ArrayList;
import java.util.List;

public class ImageRepository implements ImageDataSource {

    private static ImageRepository ourInstance;
    private List<Response> imageList;
    private ImageRemoteDataSource remoteDataSource;
    private boolean isServiceLoading;

    private ImageRepository(ImageRemoteDataSource remoteDataSource) {
        imageList = new ArrayList<>();
        this.remoteDataSource = remoteDataSource;
        isServiceLoading = false;
    }

    public static ImageRepository getInstance(ImageRemoteDataSource remoteDataSource) {
        if (ourInstance == null) {
            ourInstance = new ImageRepository(remoteDataSource);
        }
        return ourInstance;
    }

    @Override
    public void getVideosContent(NetworkStatus networkStats,
        final LoadCallBackListener callBackListener) {
        if (imageList.isEmpty()) {
            if (networkStats.isOnline()) {
                if (!isServiceLoading) {
                    isServiceLoading = true;
                    remoteDataSource.getVideosContent(networkStats, new LoadCallBackListener() {
                        @Override
                        public void onLoaded(Object response) {
                            isServiceLoading = false;
                            List<Response> responses = (List<Response>) response;
                            imageList.clear();
                            if (responses.size() > 0) {
                                imageList.addAll((List<Response>) response);
                            }
                            callBackListener.onLoaded(imageList);
                        }

                        @Override
                        public void onError(Object error) {
                            isServiceLoading = false;
                            callBackListener.onError(error);

                        }
                    });
                }
            } else {
                callBackListener.onError(ErrorCode.NETWORK_ERROR);
            }
        } else {
            callBackListener.onLoaded(imageList);
        }
    }

    @Override
    public void getParticularVideo(String id, LoadCallBackListener callBackListener) {
        if (imageList.size() > 0) {
            for(int i=0;i<imageList.size();i++) {
                if(imageList.get(i).getId().equals(id)) {
                    callBackListener.onLoaded(imageList.get(i));
                    break;
                }
            }
        } else {
            //TODO : when list empty need pass eror message
        }
    }

    @Override
    public void getRemainingContent(String id, LoadCallBackListener callBackListener) {

        if (imageList.size() > 0) {
            ArrayList<Response> dummyResponse = new ArrayList<>();
            for(int i=0;i<imageList.size();i++) {
                if(!imageList.get(i).getId().equals(id)) {
                    dummyResponse.add(imageList.get(i));
                }
            }
            callBackListener.onLoaded(dummyResponse);
        } else {
            //TODO : when list empty need pass eror message
        }
    }

}
