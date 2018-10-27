package com.geekgirl.android.baking.ui.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.geekgirl.android.baking.R;
import com.geekgirl.android.baking.databinding.FragmentStepsBinding;
import com.geekgirl.android.baking.model.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rim Gazzah on 22/10/18
 */
public class StepsFragment extends Fragment {


    private static final String STEPS_EXTRA = "steps_extra";
    public static final String TAG = "StepsFragment";
    private static final String STEP_POSITION_EXTRA = "step_position_extra";
    private static final String LAST_POSITION_EXTRA = "last_position_extra";
    private List<Step> mStepList = new ArrayList<>();
    private FragmentStepsBinding mFragmentStepsBinding;
    private int mNumPages = 0;
    private static int mPosition = -1;

    private SlidePagerAdapter mSlidePagerAdapter;

    public static StepsFragment newInstance(ArrayList<Step> stepList, int position) {
        StepsFragment stepsFragment = new StepsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(STEPS_EXTRA, stepList);
        args.putInt(STEP_POSITION_EXTRA, position);
        stepsFragment.setArguments(args);
        return stepsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentStepsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_steps, container, false);
        initData(savedInstanceState);
        initView();
        return mFragmentStepsBinding.getRoot();
    }

    public void initData(Bundle savedInstanceState) {
        if (getArguments() != null) {
            mStepList = getArguments().getParcelableArrayList(STEPS_EXTRA);
            if (savedInstanceState != null) {
                mPosition = savedInstanceState.getInt(LAST_POSITION_EXTRA);
            }
            if (mPosition == -1) {
                mPosition = getArguments().getInt(STEP_POSITION_EXTRA);
            }
            mNumPages = mStepList.size();
        }
    }

    private void initView() {
        mSlidePagerAdapter = new SlidePagerAdapter(getChildFragmentManager());
        mFragmentStepsBinding.pager.setAdapter(mSlidePagerAdapter);
        if (mPosition > -1) {
            mFragmentStepsBinding.pager.setCurrentItem(mPosition);
        }
        mFragmentStepsBinding.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(final int position) {
                setIndicator(position);
                mPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        buildCircles();
    }

    public void goToPosition(int position) {
        if (isAdded() && position < mNumPages) {
            mFragmentStepsBinding.pager.setCurrentItem(position);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPosition = -1;
    }

    private void buildCircles() {

        float scale = getResources().getDisplayMetrics().density;
        int padding = (int) (5 * scale + 0.5f);

        for (int i = 0; i < mNumPages; i++) {
            ImageView circle = new ImageView(getContext());
            circle.setImageResource(R.drawable.ic_dot);
            circle.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            circle.setAdjustViewBounds(true);
            circle.setPadding(padding, 0, padding, 0);
            mFragmentStepsBinding.circles.addView(circle);
        }
        setIndicator(mPosition);
    }


    private void setIndicator(int index) {
        if (index < mNumPages) {
            for (int i = 0; i < mNumPages; i++) {
                ImageView circle = (ImageView) mFragmentStepsBinding.circles.getChildAt(i);
                if (i == index) {
                    circle.setImageResource(R.drawable.ic_dot_selected);
                } else {
                    circle.setImageResource(R.drawable.ic_dot);
                }
            }
        }
    }

    private class SlidePagerAdapter extends FragmentStatePagerAdapter {

        public SlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return StepDetailFragment.newInstance(mStepList.get(position));
        }

        @Override
        public int getCount() {
            return mNumPages;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(LAST_POSITION_EXTRA, mPosition);
    }

}
