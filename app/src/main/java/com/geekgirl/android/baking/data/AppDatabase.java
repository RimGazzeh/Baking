package com.geekgirl.android.baking.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.geekgirl.android.baking.model.Dessert;
import com.geekgirl.android.baking.utils.Logger;

/**
 * Created by Rim Gazzah on 09/10/18
 */
@Database(entities = {Dessert.class}, version = 1, exportSchema = false)
@TypeConverters(ListConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "desserts";
    private static AppDatabase sInstance;
    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Logger.d("Creating new database instance");
                sInstance = Room.databaseBuilder(context,
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Logger.d("Getting the database instance");
        return sInstance;
    }

    public abstract DessertDAO dessertDAO();
}
