package com.geekgirl.android.baking.ui.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geekgirl.android.baking.R;
import com.geekgirl.android.baking.databinding.FragmentDessertBinding;
import com.geekgirl.android.baking.model.Dessert;
import com.geekgirl.android.baking.model.Step;
import com.geekgirl.android.baking.ui.adapters.IngredientAdapter;
import com.geekgirl.android.baking.ui.adapters.StepAdapter;

import java.util.ArrayList;

/**
 * Created by Rim Gazzah on 10/10/18
 */
public class DessertDetailFragment extends Fragment implements StepAdapter.OnStepClickListener {

    private static final String DESSERT_EXTRA = "dessert_extra";
    public static final String TAG = "DessertDetailFragment";
    private FragmentDessertBinding mBinding;
    private IngredientAdapter mIngredientAdapter;
    private StepAdapter mStepAdapter;
    private Dessert mDessert;
    private ArrayList<Step> mSteps;
    private OnStepDetailClickListener mOnStepDetailClickListener;


    public static DessertDetailFragment newInstance(Dessert dessert) {
        DessertDetailFragment dessertDetailFragment = new DessertDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(DESSERT_EXTRA, dessert);
        dessertDetailFragment.setArguments(args);
        return dessertDetailFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dessert, container, false);
        initView();
        initEvent();
        initData();
        return mBinding.getRoot();
    }

    private void initView() {
        RecyclerView.LayoutManager ingredientLayoutManager = new LinearLayoutManager(getContext());
        mBinding.recyclerviewIngredients.setLayoutManager(ingredientLayoutManager);
        mBinding.recyclerviewIngredients.setHasFixedSize(true);
        mBinding.recyclerviewIngredients.setNestedScrollingEnabled(false);
        mIngredientAdapter = new IngredientAdapter();
        mBinding.recyclerviewIngredients.setAdapter(mIngredientAdapter);

        RecyclerView.LayoutManager stepLayoutManager = new LinearLayoutManager(getContext());
        mBinding.recyclerviewSteps.setLayoutManager(stepLayoutManager);
        mBinding.recyclerviewSteps.setHasFixedSize(true);
        mBinding.recyclerviewSteps.setNestedScrollingEnabled(false);
        mStepAdapter = new StepAdapter();
        mBinding.recyclerviewSteps.setAdapter(mStepAdapter);

    }

    private void initEvent() {
        mStepAdapter.setOnStepClickListener(this);
    }

    private void initData() {
        if (getArguments() != null) {
            mDessert = getArguments().getParcelable(DESSERT_EXTRA);
            mSteps = new ArrayList<>(mDessert.getSteps());
            mIngredientAdapter.setDessertList(mDessert.getIngredients());
            mStepAdapter.setStepsList(mDessert.getSteps());
        }
    }

    @Override
    public void onStepCLick(int position) {
        mOnStepDetailClickListener.onStepDetailClick(position);
    }

    public void setOnStepDetailClickListener(OnStepDetailClickListener onStepDetailClickListener){
        mOnStepDetailClickListener = onStepDetailClickListener;
    }

    public interface OnStepDetailClickListener{
        void onStepDetailClick(int position);
    }


}
