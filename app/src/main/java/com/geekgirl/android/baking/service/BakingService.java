package com.geekgirl.android.baking.service;

import com.geekgirl.android.baking.utils.Constants;
import com.geekgirl.android.baking.utils.Logger;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Rim Gazzah on 09/10/18
 */
public class BakingService {
    private static final Object LOCK = new Object();
    private static BakingService sInstance;

    public static BakingService getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                Logger.d("Creating new BakingService instance");
                sInstance = new BakingService();
            }
        }
        Logger.d("Getting the BakingService instance");
        return sInstance;
    }

    public Call getBakingData() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Constants.BAKING_DATA_URL)
                .build();

        return client.newCall(request);
    }
}
