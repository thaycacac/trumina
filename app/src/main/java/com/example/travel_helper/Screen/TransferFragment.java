package com.example.travel_helper.Screen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_helper.Adapter.CardViewOptionsAdapter;
import com.example.travel_helper.Entity.CardItemEntity;
import com.example.travel_helper.R;
import com.example.travel_helper.Screen.checkListScreen.DashboardActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransferFragment extends Fragment implements CardViewOptionsAdapter.OnItemClickListener {

    @BindView(R.id.utility_options_recycle_view)
    RecyclerView mUtilityOptionsRecycleView;
    private Activity mActivity;
    private boolean mHasMagneticSensor = true;

    public TransferFragment() {
        // Required empty public constructor
    }


    public static TransferFragment newInstance() {
        TransferFragment fragment = new TransferFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);

        ButterKnife.bind(this, view);

        List<CardItemEntity> cardEntities = getUtilityItems();

        CardViewOptionsAdapter cardViewOptionsAdapter = new CardViewOptionsAdapter(this, cardEntities);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity.getApplicationContext());
        mUtilityOptionsRecycleView.setLayoutManager(mLayoutManager);
        mUtilityOptionsRecycleView.setItemAnimator(new DefaultItemAnimator());
        mUtilityOptionsRecycleView.setAdapter(cardViewOptionsAdapter);

        PackageManager mManager = getActivity().getPackageManager();
        boolean hasAccelerometer = mManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER);
        boolean hasMagneticSensor = mManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_COMPASS);
        if (!hasAccelerometer || !hasMagneticSensor) {
            this.mHasMagneticSensor = false;
        }

        return view;
    }


    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.mActivity = (Activity) activity;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent;
        switch (position) {
            case 0:
                intent = DashboardActivity.getStartIntent(mActivity);
                startActivity(intent);
                break;
            case 1:
                intent = WeatherActivity.getStartIntent(mActivity);
                startActivity(intent);
                break;
            case 2:
                intent = CompassActivity.getStartIntent(mActivity);
                startActivity(intent);
                break;
            case 3:
                intent = CurrencyActivity.getStartIntent(mActivity);
                startActivity(intent);
                break;
            case 4:
                intent = WorldClockActivity.getStartIntent(mActivity);
                startActivity(intent);
                break;
            case 5:
                intent = UpcomingWeekendsActivity.getStartIntent(mActivity);
                startActivity(intent);
                break;
        }
    }

    private List<CardItemEntity> getUtilityItems() {
        List<CardItemEntity> cardEntities = new ArrayList<>();
        cardEntities.add(
                new CardItemEntity(
                        getResources().getDrawable(R.drawable.checklist),
                        getResources().getString(R.string.text_checklist)));
        cardEntities.add(
                new CardItemEntity(
                        getResources().getDrawable(R.drawable.weather),
                        getResources().getString(R.string.text_weather)));
        if (mHasMagneticSensor) {
            cardEntities.add(
                    new CardItemEntity(
                            getResources().getDrawable(R.drawable.compass),
                            getResources().getString(R.string.text_Compass)));
        }
        cardEntities.add(
                new CardItemEntity(
                        getResources().getDrawable(R.drawable.currency),
                        getResources().getString(R.string.text_currency)));
        cardEntities.add(
                new CardItemEntity(
                        getResources().getDrawable(R.drawable.worldclock),
                        getResources().getString(R.string.text_clock)));
        cardEntities.add(
                new CardItemEntity(
                        getResources().getDrawable(R.drawable.upcoming_long_weekends),
                        getResources().getString(R.string.upcoming_long_weekends)));
        if (!mHasMagneticSensor) {
            cardEntities.add(
                    new CardItemEntity(
                            getResources().getDrawable(R.drawable.compass_unavailable),
                            getResources().getString(R.string.text_Compass)));
        }
        return cardEntities;
    }


}
