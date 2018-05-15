package com.pajakmedan.pajakmedan.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pajakmedan.pajakmedan.R;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.ButterKnife;

/**
 * Created by milha on 3/28/2018.
 */

public abstract class BaseFragment extends Fragment {

    protected Locale locale;
    protected NumberFormat numberFormat;

    public BaseFragment() {
        this.locale = new Locale("id", "ID");
        this.numberFormat = NumberFormat.getInstance(locale);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    abstract int getLayoutId();
}
