package com.ahmadrosid.customrequestretrofit.api;

import java.util.HashMap;
import java.util.Map;


import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by ocittwo on 9/23/17.
 */
public interface ApiServices {

    @POST("/index.php")
    Flowable<ResponseModel> sendData(@Body RequestBody body);
}
