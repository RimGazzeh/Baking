package com.geekgirl.android.baking.data;

import android.arch.persistence.room.TypeConverter;

import com.geekgirl.android.baking.model.Ingredient;
import com.geekgirl.android.baking.model.Step;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Rim Gazzah on 10/10/18
 */
public class ListConverter {
    @TypeConverter
    public static List<Ingredient> ingredientsFromString(String value) {
        Type listType = new TypeToken<List<Ingredient>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static List<Step> stepsFromString(String value) {
        Type listType = new TypeToken<List<Step>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromIngredientList(List<Ingredient> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static String fromStepsList(List<Step> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
