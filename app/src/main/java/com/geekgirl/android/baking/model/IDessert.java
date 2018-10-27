package com.geekgirl.android.baking.model;

import java.util.List;

/**
 * Created by Rim Gazzah on 09/10/18
 */
public interface IDessert {
    Long getId();
    String getName();
    String getImage();
    List<Ingredient> getIngredients();
    Long getServings();
    List<Step> getSteps();
    boolean isSaved();
}
