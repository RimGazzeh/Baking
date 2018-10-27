package com.geekgirl.android.baking.ui.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.geekgirl.android.baking.R;
import com.geekgirl.android.baking.databinding.ItemStepBinding;
import com.geekgirl.android.baking.model.IStep;

import java.util.List;

/**
 * Created by Rim Gazzah on 12/10/18
 */
public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {
    private List<? extends IStep> mStepsList;
    private OnStepClickListener mOnStepClickListener;

    public void setOnStepClickListener(OnStepClickListener onStepClickListener) {
        this.mOnStepClickListener = onStepClickListener;
    }

    public void setStepsList(List<? extends IStep> mStepsList) {
        this.mStepsList = mStepsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        ItemStepBinding itemStepBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.item_step, viewGroup, false);
        return new StepViewHolder(itemStepBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder stepViewHolder, int position) {
        IStep iStep = mStepsList.get(position);
        stepViewHolder.mItemStepBinding.setStep(iStep);
        stepViewHolder.mItemStepBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mStepsList != null ? mStepsList.size() : 0;
    }

    public interface OnStepClickListener {
        void onStepCLick(int position);
    }

    class StepViewHolder extends RecyclerView.ViewHolder {

        ItemStepBinding mItemStepBinding;

        public StepViewHolder(ItemStepBinding itemStepBinding) {
            super(itemStepBinding.getRoot());
            mItemStepBinding = itemStepBinding;
            mItemStepBinding.itemContainer.setOnClickListener(view -> {
                if (mOnStepClickListener != null) {
                    mOnStepClickListener.onStepCLick(getAdapterPosition());
                }
            });
        }
    }
}
