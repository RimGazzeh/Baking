package com.geekgirl.android.baking.ui.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.geekgirl.android.baking.R;
import com.geekgirl.android.baking.databinding.ActivityMainBinding;
import com.geekgirl.android.baking.model.Dessert;
import com.geekgirl.android.baking.service.AppExecutors;
import com.geekgirl.android.baking.ui.adapters.DessertAdapter;
import com.geekgirl.android.baking.utils.Constants;
import com.geekgirl.android.baking.utils.Logger;
import com.geekgirl.android.baking.utils.SimpleIdlingResource;
import com.geekgirl.android.baking.viewModel.BakingViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BakingViewModel mViewModel;
    private ActivityMainBinding mBinding;
    private DessertAdapter mDessertAdapter;
    private List<Dessert> mDessertList = new ArrayList<>();

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }


    private BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
            if (isConnected) {
                mBinding.noNetworkContainer.setVisibility(View.GONE);
                refreshData();
            }else {
                getDataOffline();
            }
        }
    };
    private IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);


    public void getDataOffline(){
        if(mDessertList== null || mDessertList.isEmpty()){
            AppExecutors.getInstance().diskIO().execute(() -> {
                mViewModel.getSavedDeserts().observe(MainActivity.this, desserts -> {
                    mDessertList = desserts;
                    if(mDessertList== null || mDessertList.isEmpty()){
                        mBinding.noNetworkContainer.setVisibility(View.VISIBLE);
                    }else {
                        mBinding.noNetworkContainer.setVisibility(View.GONE);
                        mDessertAdapter.setDessertList(mDessertList);
                    }
                });
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mViewModel = ViewModelProviders.of(this).get(BakingViewModel.class);
        getIdlingResource();
        initView();
        initEvent();
        refreshData();
    }

    private void initView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mBinding.recyclerviewDesserts.setLayoutManager(mLayoutManager);
        mBinding.recyclerviewDesserts.setItemAnimator(new DefaultItemAnimator());
        mBinding.recyclerviewDesserts.setHasFixedSize(true);
        mDessertAdapter = new DessertAdapter();
        mBinding.recyclerviewDesserts.setAdapter(mDessertAdapter);
    }

    private void initEvent() {
        this.registerReceiver(networkStateReceiver, filter);
        mDessertAdapter.setOnDessertClickListener(position -> {
            Intent intent = new Intent(this, DetailActivity.class);
            Dessert dessert = mDessertList.get(position);
            intent.putExtra(Constants.DESSERT_EXTRA, dessert);
            startActivity(intent);
        });
    }

    private void refreshData() {
        mViewModel.getDeserts().observe(this, desserts -> {
            mDessertList = desserts;
            if (mDessertList != null) {
                Logger.d(mDessertList.toString());
            } else {
                Logger.d("mDessertList is null");
            }
            mDessertAdapter.setDessertList(mDessertList);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(networkStateReceiver);

    }
}
