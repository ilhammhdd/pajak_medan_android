package com.pajakmedan.pajakmedan;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.orhanobut.hawk.Hawk;

import butterknife.ButterKnife;

/**
 * Created by milha on 2/19/2018.
 */

public abstract class BaseActivity extends AppCompatActivity {

    static DisplayMetrics displaymetrics = new DisplayMetrics();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentId());
        ButterKnife.bind(this);
        Hawk.init(this).build();

        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        insideOnCreate();
    }

    abstract int getContentId();

    abstract void insideOnCreate();

    public static int getDeviceWidth() {
        return displaymetrics.widthPixels;
    }

    public static int getDeviceHeight() {
        return displaymetrics.heightPixels;
    }
}
