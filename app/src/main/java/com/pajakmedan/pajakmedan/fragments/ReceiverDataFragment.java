package com.pajakmedan.pajakmedan.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.R;
import com.pajakmedan.pajakmedan.models.Profile;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by milha on 3/28/2018.
 */

public class ReceiverDataFragment extends Fragment {

    @BindView(R.id.cardView_basket_receiverData)
    public CardView cardViewReceiverData;
    @BindView(R.id.textView_receiver_name)
    TextView textViewName;
    @BindView(R.id.textView_receiver_phone)
    TextView textViewPhone;
    @BindView(R.id.textView_receiver_email)
    TextView textViewEmail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receiver, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Profile profile = Hawk.get(Constants.PROFILE_KEY);
        textViewName.setText(profile.fullName);
        textViewPhone.setText(profile.phoneNumber);
        textViewEmail.setText(profile.email);
    }
}
