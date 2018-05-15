package com.pajakmedan.pajakmedan.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.R;
import com.pajakmedan.pajakmedan.models.Basket;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

/**
 * Created by milha on 3/28/2018.
 */

public class TotalPriceFragment extends BaseFragment {

    @BindView(R.id.cardView_basket_totalPrice)
    public CardView cardViewTotalPrice;
    @BindView(R.id.textView_basket_totalBasketPrice)
    TextView textViewTotalPrice;

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_total_price, container, false);
//        ButterKnife.bind(this, view);
//        return view;
//    }

    @Override
    int getLayoutId() {
        return R.layout.fragment_total_price;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Basket basket = Hawk.get(Constants.BASKET_KEY);
        textViewTotalPrice.setText(this.numberFormat.format(basket.total));
    }
}
