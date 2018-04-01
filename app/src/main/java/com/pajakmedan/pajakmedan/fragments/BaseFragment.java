package com.pajakmedan.pajakmedan.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by milha on 3/28/2018.
 */

public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ButterKnife.bind(this, inflater.inflate(getLayoutId(), container));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    abstract int getLayoutId();
}
