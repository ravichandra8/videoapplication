package com.mobiotics.videoapplication.modal;

import com.mobiotics.videoapplication.Util.ErrorCode;
import com.mobiotics.videoapplication.Util.NetworkStatus;
import com.mobiotics.videoapplication.modal.pojo.Response;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;

public class ImageRemoteDataSource implements ImageDataSource {

    private static ImageRemoteDataSource ourInstance;
    private ImageServices apiService;

    private ImageRemoteDataSource() {
    }

    public static ImageRemoteDataSource getInstance() {
        if (ourInstance == null) {
            ourInstance = new ImageRemoteDataSource();
        }
        return ourInstance;
    }

    @Override
    public void getVideosContent(NetworkStatus networkStats, final LoadCallBackListener callBackListener) {
        apiService = RetrofitClient.getClient().create(ImageServices.class);

        Call<List<Response>> call = apiService.getContent("pretty");
        call.enqueue(new Callback<List<Response>>() {
            @Override
            public void onResponse(Call<List<Response>> call,
                retrofit2.Response<List<Response>> response) {
                if (response.isSuccessful()) {
                    callBackListener.onLoaded(response.body());
                } else {
                    callBackListener.onError(ErrorCode.CONNECTION_PROBLEM);

                }
            }

            @Override
            public void onFailure(Call<List<Response>> call, Throwable t) {
                callBackListener.onError(ErrorCode.CONNECTION_PROBLEM);
            }
        });

    }

    @Override
    public void getParticularVideo(String id, LoadCallBackListener callBackListener) {

    }

    @Override
    public void getRemainingContent(String id, LoadCallBackListener callBackListener) {

    }


}
