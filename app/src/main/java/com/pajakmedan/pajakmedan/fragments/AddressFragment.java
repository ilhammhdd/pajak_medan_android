package com.pajakmedan.pajakmedan.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.AddressesActivity;
import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.R;
import com.pajakmedan.pajakmedan.models.Address;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by milha on 3/28/2018.
 */

public class AddressFragment extends BaseFragment {

    @BindView(R.id.textView_basket_address)
    TextView textViewAddress;
    @BindView(R.id.cardView_basket_address)
    public CardView cardViewAddress;

    @Override
    int getLayoutId() {
        return R.layout.fragment_address;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMainAddress();
        Address mainAddress = Hawk.get(Constants.MAIN_ADDRESS_KEY);
        if (mainAddress.name.equals("")) {
            textViewAddress.setText(getResources().getString(R.string.tidak_ada_alamat_utama));
            textViewAddress.setTextColor(getResources().getColor(R.color.colorRedAlert));
            return;
        }
        textViewAddress.setText(mainAddress.name);
        textViewAddress.setTextColor(getResources().getColor(R.color.colorBlack));
    }

    @OnClick(R.id.button_basket_diffAddress)
    void diffAddressClicked() {
        startActivity(new Intent(getActivity(), AddressesActivity.class));
    }
}
