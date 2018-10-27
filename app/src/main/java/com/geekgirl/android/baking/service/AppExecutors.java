package com.geekgirl.android.baking.service;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Rim Gazzah on 23/10/18
 */
public class AppExecutors {

    private static final Object LOCK = new Object();
    private static AppExecutors sInstance;
    private final Executor diskIO;

    private AppExecutors(Executor diskIO) {
        this.diskIO = diskIO;
    }

    public static AppExecutors getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AppExecutors(Executors.newSingleThreadExecutor());
            }
        }
        return sInstance;
    }
    public Executor diskIO() {
        return diskIO;
    }

}
