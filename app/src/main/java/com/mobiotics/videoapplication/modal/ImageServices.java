package com.mobiotics.videoapplication.modal;

import com.mobiotics.videoapplication.modal.pojo.Response;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ImageServices {


    @GET("media.json")
    Call<List<Response>> getContent(@Query("print") String apiKey);


}
