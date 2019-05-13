package com.redbuffer.redbufferassignment.communication.api;

import com.redbuffer.redbufferassignment.Constants.ApiConstants;
import com.redbuffer.redbufferassignment.communication.CommonResponse;
import com.redbuffer.redbufferassignment.model.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserLocationApi {
    @GET(ApiConstants.USER_LOCATION)
    Call<ArrayList<User>> getAllLocations();

    @POST(ApiConstants.USER_LOCATION)
    Call<CommonResponse> saveUserLocation(@Body User user );
}
