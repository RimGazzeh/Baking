package com.geekgirl.android.baking.ui.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.geekgirl.android.baking.R;
import com.geekgirl.android.baking.databinding.ItemIngredientBinding;
import com.geekgirl.android.baking.model.IIngredient;

import java.util.List;

/**
 * Created by Rim Gazzah on 10/10/18
 */
public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private List<? extends IIngredient> mIngredientList;

    public void setDessertList(final List<? extends IIngredient> ingredientsList) {
        mIngredientList = ingredientsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        ItemIngredientBinding itemIngredientBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.item_ingredient, viewGroup, false);
        return new IngredientViewHolder(itemIngredientBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        IIngredient iIngredient = mIngredientList.get(position);
        holder.itemIngredientBinding.setIngredient(iIngredient);
        holder.itemIngredientBinding.executePendingBindings();

    }

    @Override
    public int getItemCount() {
        return mIngredientList == null ? 0 : mIngredientList.size();
    }



    class IngredientViewHolder extends RecyclerView.ViewHolder {

        ItemIngredientBinding itemIngredientBinding;

        public IngredientViewHolder(ItemIngredientBinding binding) {
            super(binding.getRoot());
            itemIngredientBinding = binding;
        }
    }
}
