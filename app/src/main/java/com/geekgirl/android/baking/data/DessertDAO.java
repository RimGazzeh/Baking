package com.geekgirl.android.baking.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.geekgirl.android.baking.model.Dessert;

import java.util.List;

/**
 * Created by Rim Gazzah on 09/10/18
 */
@Dao
public interface DessertDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertDessert(Dessert dessert);

    @Query("UPDATE dessert SET isSaved = 0 WHERE isSaved = 1")
    long unSaveAll();

    @Query("SELECT * FROM dessert" )
    LiveData<List<Dessert>> getAllDesserts();

    @Query("SELECT * FROM dessert WHERE isSaved = 1" )
    List<Dessert> getSavedDessert();

    @Query("SELECT * FROM dessert WHERE id = :id")
    Dessert getDessertById(Long id);
}
