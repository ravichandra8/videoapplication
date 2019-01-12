package com.mobiotics.videoapplication.modal;

import android.database.Cursor;

import com.mobiotics.videoapplication.Util.ErrorCode;
import com.mobiotics.videoapplication.Util.NetworkStatus;
import com.mobiotics.videoapplication.modal.pojo.Video;
import java.util.ArrayList;
import java.util.List;

public class VideoRepository implements VideoDataSource {

    private static VideoRepository ourInstance;
    private List<Video> videoList;
    private ArrayList<Video> relatedVideos;
    private VideoRemoteDataSource remoteDataSource;
    private boolean isServiceLoading;
    private static MydatabaseAdapter database;

    private VideoRepository(VideoRemoteDataSource remoteDataSource) {
        videoList = new ArrayList<>();
        relatedVideos = new ArrayList<>();
        this.remoteDataSource = remoteDataSource;
        isServiceLoading = false;

    }

    public static VideoRepository getInstance(VideoRemoteDataSource remoteDataSource, MydatabaseAdapter mydatabaseAdapter) {
        if (ourInstance == null) {
            ourInstance = new VideoRepository(remoteDataSource);
            database=mydatabaseAdapter;
        }
        return ourInstance;
    }

    @Override
    public void getVideosContent(NetworkStatus networkStats,
        final LoadCallBackListener callBackListener) {
        if (videoList.isEmpty()) {
            if (networkStats.isOnline()) {
                if (!isServiceLoading) {
                    isServiceLoading = true;
                    remoteDataSource.getVideosContent(networkStats, new LoadCallBackListener() {
                        @Override
                        public void onLoaded(Object response) {
                            isServiceLoading = false;
                            List<Video> videoList1 = (List<Video>) response;
                            videoList.clear();
                            if (videoList1.size() > 0) {
                                videoList.addAll(videoList1);
                            }
                            callBackListener.onLoaded(videoList);
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
            callBackListener.onLoaded(videoList);
        }
    }

    @Override
    public void getParticularVideo(String id,boolean isNext,LoadCallBackListener callBackListener) {
        if (videoList.size() > 0) {
            for(int i = 0; i< videoList.size(); i++) {
                if(videoList.get(i).getId().equals(id)) {
                    if(isNext) {
                        i = i + 1;
                    }
                    //when last item then we are resetting to 1st position
                    if(i == videoList.size()){
                        i=0;
                    }

                    Cursor c =database.getParticularVideoPosition(videoList.get(i).getId());
                    while (c.moveToNext()) {
                        videoList.get(i).setVideoDuration(c.getLong(1));
                    }
                    callBackListener.onLoaded(videoList.get(i));
                    break;
                }
            }
        } else {
           callBackListener.onError(ErrorCode.NO_RESULT);
        }
    }

    @Override
    public void getRelatedVideos(String id,LoadCallBackListener callBackListener) {

        if (videoList.size() > 0) {
            relatedVideos.clear();
            relatedVideos.addAll(videoList);
            for(int i = 0; i< videoList.size(); i++) {
                if (videoList.get(i).getId().equals(id)) {
                    relatedVideos.remove(i);
                    break;
                }
            }
            callBackListener.onLoaded(relatedVideos);
        } else {
            callBackListener.onError(ErrorCode.NO_RESULT);
        }
    }

    public void insertVideoPosition(String id,long videoDuration,boolean videoState){
        database.insertVideoPosition(id,videoDuration,videoState);
    }
    public void deleteVideoPosition(String id){
        if(videoList.size()>0){
            for(int i = 0; i< videoList.size(); i++) {
                if (videoList.get(i).getId().equals(id)) {
                    videoList.get(i).setVideoDuration(0);
                    break;
                }
            }
        }
        database.deleteParticularVideo(id);
    }
}
