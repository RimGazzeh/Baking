package com.geekgirl.android.baking.ui.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.geekgirl.android.baking.R;
import com.geekgirl.android.baking.databinding.ItemRecipeBinding;
import com.geekgirl.android.baking.model.IDessert;

import java.util.List;

/**
 * Created by Rim Gazzah on 10/10/18
 */
public class DessertAdapter extends RecyclerView.Adapter<DessertAdapter.DessertViewHolder> {

    private List<? extends IDessert> mDessertList;
    private OnDessertClickListener mDessertClickListener;

    public void setDessertList(final List<? extends IDessert> dessertList) {
        mDessertList = dessertList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DessertViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        ItemRecipeBinding itemRecipeBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.item_recipe, viewGroup, false);
        return new DessertViewHolder(itemRecipeBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DessertViewHolder holder, int position) {
        IDessert iDessert = mDessertList.get(position);
        holder.itemRecipeBinding.setDessert(iDessert);
        holder.itemRecipeBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mDessertList == null ? 0 : mDessertList.size();
    }

    public void setOnDessertClickListener(OnDessertClickListener onDessertClickListener){
        mDessertClickListener = onDessertClickListener;
    }

    public interface OnDessertClickListener{
        void onDessertCLick(int position);
    }

    class DessertViewHolder extends RecyclerView.ViewHolder {

        ItemRecipeBinding itemRecipeBinding;

        public DessertViewHolder(ItemRecipeBinding binding) {
            super(binding.getRoot());
            itemRecipeBinding = binding;
            itemRecipeBinding.itemContainer.setOnClickListener(view -> {
                if(mDessertClickListener != null){
                    mDessertClickListener.onDessertCLick(getAdapterPosition());
                }
            });
        }
    }
}
