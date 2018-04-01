package com.pajakmedan.pajakmedan.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pajakmedan.pajakmedan.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by milha on 3/28/2018.
 */

public class AddressFragment extends Fragment {

    @BindView(R.id.cardView_basket_address)
    public CardView cardViewAddress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
