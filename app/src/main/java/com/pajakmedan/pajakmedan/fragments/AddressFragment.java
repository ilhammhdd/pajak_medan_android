package com.pajakmedan.pajakmedan.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.AddressesActivity;
import com.pajakmedan.pajakmedan.BaseActivity;
import com.pajakmedan.pajakmedan.BasketActivity;
import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.R;
import com.pajakmedan.pajakmedan.asynctasks.GetMainAddress;
import com.pajakmedan.pajakmedan.models.Address;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
