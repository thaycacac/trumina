package com.example.travel_helper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    ImageView ic_logo;
    SplashScreen activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);


        activity = this;
        ic_logo = findViewById(R.id.ic_logo);
        ic_logo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_in));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ic_logo.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.splash_out));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ic_logo.setVisibility(View.GONE);
                        startActivity(new Intent(activity, MainActivity.class));
                        finish();
                    }
                }, 500);
            }
        }, 1500);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
