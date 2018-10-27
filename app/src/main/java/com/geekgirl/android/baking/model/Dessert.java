
package com.geekgirl.android.baking.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.Objects;


@Entity(tableName = "dessert")
public class Dessert implements IDessert, Parcelable {

    @PrimaryKey
    @NonNull
    private Long id;
    private String image;
    private List<Ingredient> ingredients;
    private String name;
    private Long servings;
    private List<Step> steps;

    private boolean isSaved =false;

    public Dessert(){
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getServings() {
        return servings;
    }

    public void setServings(Long servings) {
        this.servings = servings;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    @Override
    public String toString() {
        return "Dessert{" +
                "id=" + id +
                ", image='" + image + '\'' +
                ", ingredients=" + ingredients +
                ", name='" + name + '\'' +
                ", servings=" + servings +
                ", steps=" + steps +
                ", isSaved=" + isSaved +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dessert dessert = (Dessert) o;
        return Objects.equals(getId(), dessert.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.image);
        dest.writeTypedList(this.ingredients);
        dest.writeString(this.name);
        dest.writeValue(this.servings);
        dest.writeTypedList(this.steps);
        dest.writeByte(this.isSaved ? (byte) 1 : (byte) 0);
    }

    protected Dessert(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.image = in.readString();
        this.ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        this.name = in.readString();
        this.servings = (Long) in.readValue(Long.class.getClassLoader());
        this.steps = in.createTypedArrayList(Step.CREATOR);
        this.isSaved = in.readByte() != 0;
    }

    public static final Creator<Dessert> CREATOR = new Creator<Dessert>() {
        @Override
        public Dessert createFromParcel(Parcel source) {
            return new Dessert(source);
        }

        @Override
        public Dessert[] newArray(int size) {
            return new Dessert[size];
        }
    };
}
