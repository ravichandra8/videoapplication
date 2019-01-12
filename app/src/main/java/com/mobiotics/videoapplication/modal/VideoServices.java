package com.mobiotics.videoapplication.modal;

import com.mobiotics.videoapplication.modal.pojo.Video;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VideoServices {
    @GET("media.json")
    Call<List<Video>> getContent(@Query("print") String apiKey);
}
