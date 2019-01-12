package com.mobiotics.videoapplication.modal;

import com.mobiotics.videoapplication.Util.ErrorCode;
import com.mobiotics.videoapplication.Util.NetworkStatus;
import com.mobiotics.videoapplication.modal.pojo.Video;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;

public class VideoRemoteDataSource implements VideoDataSource {

    private static VideoRemoteDataSource ourInstance;
    private VideoServices apiService;

    private VideoRemoteDataSource() {
    }

    public static VideoRemoteDataSource getInstance() {
        if (ourInstance == null) {
            ourInstance = new VideoRemoteDataSource();
        }
        return ourInstance;
    }

    @Override
    public void getVideosContent(NetworkStatus networkStats, final LoadCallBackListener callBackListener) {
        apiService = RetrofitClient.getClient().create(VideoServices.class);

        Call<List<Video>> call = apiService.getContent("pretty");
        call.enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call,
                retrofit2.Response<List<Video>> response) {
                if (response.isSuccessful()) {
                    callBackListener.onLoaded(response.body());
                } else {
                    callBackListener.onError(ErrorCode.CONNECTION_PROBLEM);

                }
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                callBackListener.onError(ErrorCode.CONNECTION_PROBLEM);
            }
        });

    }

    @Override
    public void getParticularVideo(String id, boolean isNext,LoadCallBackListener callBackListener) {

    }

    @Override
    public void getRelatedVideos(String id, LoadCallBackListener callBackListener) {

    }


}
