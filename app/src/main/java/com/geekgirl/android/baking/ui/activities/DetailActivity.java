package com.geekgirl.android.baking.ui.activities;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.geekgirl.android.baking.R;
import com.geekgirl.android.baking.data.AppDatabase;
import com.geekgirl.android.baking.databinding.ActivityDetailBinding;
import com.geekgirl.android.baking.model.Dessert;
import com.geekgirl.android.baking.model.Step;
import com.geekgirl.android.baking.service.AppExecutors;
import com.geekgirl.android.baking.ui.fragments.DessertDetailFragment;
import com.geekgirl.android.baking.ui.fragments.StepsFragment;
import com.geekgirl.android.baking.ui.widget.IngredientsWidgetProvider;
import com.geekgirl.android.baking.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rim Gazzah on 23/10/18
 */
public class DetailActivity extends AppCompatActivity implements DessertDetailFragment.OnStepDetailClickListener {
    private static final String LAST_STEP_EXTRA = "last_step_extra";
    private ActivityDetailBinding activityDetailBinding;
    private Dessert mDessert;
    private StepsFragment mStepsFragment;
    private ArrayList<Step> mSteps = new ArrayList<>();
    private static boolean isHome = true;
    private AppDatabase mDb;
    private static int mPosition = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView(savedInstanceState);
    }

    private void initData() {
        mDb = AppDatabase.getInstance(this);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constants.DESSERT_EXTRA)) {
            mDessert = intent.getParcelableExtra(Constants.DESSERT_EXTRA);
            if (!mDessert.getSteps().isEmpty()) {
                mSteps = new ArrayList<>(mDessert.getSteps());
            }
        }
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<Dessert> dessertList = mDb.dessertDAO().getSavedDessert();
            if (dessertList != null && dessertList.size() > 0) {
                if(dessertList.get(0).getId() == mDessert.getId()){
                    mDessert.setSaved(true);
                }
            }
        });
        }

    private boolean isBigDevice() {
        return activityDetailBinding.recipeDetailContainer != null;
    }

    private void initView(Bundle savedInstanceState) {
        activityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        getSupportActionBar().setTitle(mDessert.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        goToDessertDetailFragment(mDessert);
        if (savedInstanceState != null && isHome == false) {
            int lastPosition = savedInstanceState.getInt(LAST_STEP_EXTRA);
            goToStepsFragment(mSteps, lastPosition);
        } else {
            if (isBigDevice()) {
                goToStepsFragment(mSteps, 0);
            }
        }
    }

    private void goToDessertDetailFragment(Dessert dessert) {
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        DessertDetailFragment mDessertDetailFragment = DessertDetailFragment.newInstance(dessert);
        mDessertDetailFragment.setOnStepDetailClickListener(this::onStepDetailClick);
        if (isBigDevice()) {
            fragmentTransaction.replace(R.id.recipe_detail_container, mDessertDetailFragment, DessertDetailFragment.TAG).addToBackStack(DessertDetailFragment.TAG);
        } else {
            fragmentTransaction.replace(R.id.details_container, mDessertDetailFragment, DessertDetailFragment.TAG).addToBackStack(DessertDetailFragment.TAG);
        }
        fragmentTransaction.commit();
    }

    private void goToStepsFragment(ArrayList<Step> stepList, int position) {
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        mStepsFragment = StepsFragment.newInstance(stepList, position);
        if (isBigDevice()) {
            fragmentTransaction.replace(R.id.recipe_steps_container, mStepsFragment, StepsFragment.TAG).addToBackStack(StepsFragment.TAG);
        } else {
            fragmentTransaction.replace(R.id.details_container, mStepsFragment, StepsFragment.TAG).addToBackStack(StepsFragment.TAG);
            isHome = false;
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onStepDetailClick(int position) {
        if (mStepsFragment == null && !isBigDevice()) {
            goToStepsFragment(mSteps, position);
        } else {
            mStepsFragment.goToPosition(position);
        }
        mPosition = position;
    }

    @Override
    public void onBackPressed() {
        if (isBigDevice() || isHome) {
            finish();
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
                mStepsFragment = null;
            }
            isHome = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dessert_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem itemSave = menu.findItem(R.id.menu_save);
        if (mDessert.isSaved()) {
            itemSave.setIcon(R.drawable.ic_unsave);
        } else {
            itemSave.setIcon(R.drawable.ic_save);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPosition > -1) {
            outState.putInt(LAST_STEP_EXTRA, mPosition);
        }
    }

    public void saveDessert() {
        mDessert.setSaved(true);
        AppExecutors.getInstance().diskIO().execute(() -> {
            mDb.dessertDAO().unSaveAll();
            mDb.dessertDAO().insertDessert(mDessert);
        });
    }

    public void unSaveDessert() {
        mDessert.setSaved(false);
        AppExecutors.getInstance().diskIO().execute(() -> {
            mDb.dessertDAO().insertDessert(mDessert);
        });
    }
    public void updateWidget(){
        Intent intent = new Intent(this, IngredientsWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication())
                .getAppWidgetIds(new ComponentName(getApplication(), IngredientsWidgetProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_save: {
                if (mDessert.isSaved()) {
                    unSaveDessert();
                } else {
                    saveDessert();
                }
                updateWidget();
                invalidateOptionsMenu();
                break;
            }
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
