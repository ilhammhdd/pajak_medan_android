package com.pajakmedan.pajakmedan.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.R;
import com.pajakmedan.pajakmedan.models.Basket;

import butterknife.BindView;

/**
 * Created by milha on 3/28/2018.
 */

public class TotalPriceFragment extends BaseFragment {

    @BindView(R.id.cardView_basket_totalPrice)
    public CardView cardViewTotalPrice;
    @BindView(R.id.textView_basket_totalBasketPrice)
    TextView textViewTotalPrice;

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
