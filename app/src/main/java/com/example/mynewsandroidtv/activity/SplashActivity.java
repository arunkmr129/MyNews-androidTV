package com.example.mynewsandroidtv.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.mynewsandroidtv.R;
import com.example.mynewsandroidtv.interfaceCallback.DialogBtnPressResponse;
import com.example.mynewsandroidtv.model.MainDataModel;
import com.example.mynewsandroidtv.networkIntefaces.APIClient;
import com.example.mynewsandroidtv.networkIntefaces.APIInterface;
import com.example.mynewsandroidtv.utility.MyApplication;
import com.example.mynewsandroidtv.utility.UtilityClass;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends Activity implements DialogBtnPressResponse {

    private MainDataModel model;
    private APIInterface apiInterface;
    private UtilityClass utilityClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        utilityClass = new UtilityClass(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
//        fetchData();
        getData();
    }

    private void fetchData() {
        Call<MainDataModel> call = apiInterface.doGetListResources();
        call.enqueue(new Callback<MainDataModel>() {
            @Override
            public void onResponse(Call<MainDataModel> call, Response<MainDataModel> response) {
                model = response.body();
                goToMainActivity();
            }

            @Override
            public void onFailure(Call<MainDataModel> call, Throwable t) {
                call.cancel();
                utilityClass.showDialogWithTwoButton(SplashActivity.this, 0, "errorMessage");
            }
        });
    }

    private void goToMainActivity() {
        int SPLASH_TIME_OUT = 5000;
        new Handler().postDelayed(() -> {
            MyApplication.setMainDataModel(model);
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void dialogLeftButtonPressed(Dialog dialog, int tag) {

    }

    @Override
    public void dialogExit(Dialog dialog, int tag) {

    }

    @Override
    public void dialogRightButtonPressed(Dialog dialog, int tag) {

    }

    private void getData() {
        Gson gson = new Gson();
        model = gson.fromJson(utilityClass.loadJSONFromAsset(), MainDataModel.class);
        goToMainActivity();
    }

}