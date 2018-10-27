package com.geekgirl.android.baking.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.geekgirl.android.baking.data.AppDatabase;
import com.geekgirl.android.baking.model.Dessert;
import com.geekgirl.android.baking.service.BakingService;
import com.geekgirl.android.baking.utils.Logger;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Rim Gazzah on 09/10/18
 */
public class BakingViewModel extends AndroidViewModel {

    private AppDatabase mAppDatabase;
    private BakingService mBakingService;
    private MutableLiveData<List<Dessert>> mDesserts;
    private LiveData<List<Dessert>> mSavedDessert;

    public BakingViewModel(@NonNull Application application) {
        super(application);
        mAppDatabase = AppDatabase.getInstance(application);
        mBakingService = BakingService.getInstance();
    }

    public MutableLiveData<List<Dessert>> getDeserts() {
        if (mDesserts == null) {
            mDesserts = new MutableLiveData<>();
            loadDesserts();
        }
        return mDesserts;
    }

    public LiveData<List<Dessert>> getSavedDeserts() {
        if (mSavedDessert == null) {
            mSavedDessert = new MutableLiveData<>();
            mSavedDessert = mAppDatabase.dessertDAO().getAllDesserts();
        }
        return mSavedDessert;
    }

    public static List<Dessert> parseDessertData(String dessertData) {
        Logger.d("Dessert | " + dessertData);
        Gson gson = new Gson();
        Type type = new TypeToken<List<Dessert>>() {
        }.getType();
        try {
            List<Dessert> dessertList = gson.fromJson(dessertData, type);
            return dessertList;
        } catch (JsonSyntaxException e) {
            Logger.e(e.getMessage());
            return null;
        }
    }


    private void loadDesserts() {
        Call call = mBakingService.getBakingData();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mDesserts = null;
                Logger.e(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    List<Dessert> resultDessert = parseDessertData(response.body().string());
                    Logger.d("Dessert | " + resultDessert != null ? resultDessert.toString() : "resultDessert is null");
                    List<Dessert> dessertList = mDesserts.getValue();
                    if (dessertList == null || dessertList.isEmpty()) {
                        mDesserts.postValue(resultDessert);
                    } else {
                        dessertList.addAll(resultDessert);
                        mDesserts.postValue(dessertList);
                    }
                }
            }
        });
    }
}
