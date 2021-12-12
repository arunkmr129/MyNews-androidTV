package com.example.mynewsandroidtv.networkIntefaces;


import com.example.mynewsandroidtv.model.MainDataModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInterface {
    @GET("")
    Call<MainDataModel> doGetListResources();
}
