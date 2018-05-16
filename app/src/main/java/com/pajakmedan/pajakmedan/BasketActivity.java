package com.pajakmedan.pajakmedan;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.adapters.BasketGoodsAdapter;
import com.pajakmedan.pajakmedan.asynctasks.GetBasketGoods;
import com.pajakmedan.pajakmedan.fragments.AddressFragment;
import com.pajakmedan.pajakmedan.fragments.BasketGoodsFragment;
import com.pajakmedan.pajakmedan.fragments.ReceiverDataFragment;
import com.pajakmedan.pajakmedan.fragments.TotalPriceFragment;
import com.pajakmedan.pajakmedan.listeners.BasketActivityFragmentListener;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.models.Basket;
import com.pajakmedan.pajakmedan.models.BasketGoods;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

/**
 * Created by milha on 3/1/2018.
 */

public class BasketActivity extends BaseActivity {
    @BindView(R.id.constraintLayout_basket)
    ConstraintLayout constraintLayoutBasket;
    @BindView(R.id.button_basket_lanjutkanPembayaran)
    Button buttonContinuePayment;
    @BindView(R.id.toolbar5)
    Toolbar toolbarBasket5;
    @BindView(R.id.imageView_basket_basketEmpty)
    ImageView imageViewBasketEmpty;

    FragmentManager fragmentManager;
    static BasketActivity basketActivity;
    double frameLayoutBasketGoodsHeight;

    BasketGoodsFragment basketGoodsFragment;
    AddressFragment addressFragment;
    ReceiverDataFragment receiverDataFragment;
    TotalPriceFragment totalPriceFragment;

    @Override
    int getContentId() {
        return R.layout.activity_basket;
    }

    @Override
    void insideOnCreate() {
        basketActivity = this;
        fragmentManager = getFragmentManager();
        if (basketIsEmpty()) {
            imageViewBasketEmpty.setImageResource(R.drawable.ic_shopping_basket_grey);
            buttonContinuePayment.setText(getResources().getString(R.string.mulai_belanja));
            return;
        }
        setFragments();
        eachFragmentsListener();
        frameLayoutBasketGoodsHeight = frameLayoutBasketGoods.getLayoutParams().height;
    }

    private void setFragments() {
        basketGoodsFragment = new BasketGoodsFragment();
        addressFragment = new AddressFragment();
        receiverDataFragment = new ReceiverDataFragment();
        totalPriceFragment = new TotalPriceFragment();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayout_basket_basketGoods, basketGoodsFragment, "fragment_basket_goods");
        fragmentTransaction.add(R.id.frameLayout_basket_address, addressFragment, "fragment_address");
        fragmentTransaction.add(R.id.frameLayout_basket_receiverData, receiverDataFragment, "fragment_receiver");
        fragmentTransaction.add(R.id.frameLayout_basket_totalPrice, totalPriceFragment, "fragment_total_price");
        fragmentTransaction.commit();
    }

    @BindView(R.id.frameLayout_basket_basketGoods)
    FrameLayout frameLayoutBasketGoods;
    @BindView(R.id.frameLayout_basket_address)
    FrameLayout frameLayoutAddress;
    @BindView(R.id.frameLayout_basket_receiverData)
    FrameLayout frameLayoutReceiverData;
    @BindView(R.id.frameLayout_basket_totalPrice)
    FrameLayout frameLayoutTotalPrice;

    private void eachFragmentsListener() {
        basketGoodsFragment.setBasketActivityFragmentListener(new BasketActivityFragmentListener() {
            @Override
            public void toogleBasketGoodsExpand(boolean expanded, ImageView imageViewExpandIcon) {
                if (!expanded) {
                    imageViewExpandIcon.setImageLevel(0);
                    frameLayoutBasketGoods.getLayoutParams().height = getDeviceHeight() - toolbarBasket5.getLayoutParams().height - buttonContinuePayment.getLayoutParams().height - imageViewExpandIcon.getLayoutParams().height;
                    frameLayoutAddress.setVisibility(View.GONE);
                    frameLayoutReceiverData.setVisibility(View.GONE);
                    frameLayoutTotalPrice.setVisibility(View.GONE);
                    return;
                }
                imageViewExpandIcon.setImageLevel(1);
                frameLayoutBasketGoods.getLayoutParams().height = (int) frameLayoutBasketGoodsHeight;
                frameLayoutAddress.setVisibility(View.VISIBLE);
                frameLayoutReceiverData.setVisibility(View.VISIBLE);
                frameLayoutTotalPrice.setVisibility(View.VISIBLE);
            }
        });
    }

    @OnClick(R.id.imageView_basket_back)
    void backPressed() {
        finish();
    }

    @OnClick(R.id.button_basket_lanjutkanPembayaran)
    void lanjutPembayaran() {
        if (basketIsEmpty()) {
            startActivity(new Intent(BasketActivity.this, CustomerHomeActivity.class));
            return;
        }
        startActivity(new Intent(BasketActivity.this, PaymentActivity.class));
    }

    boolean basketIsEmpty() {
        Basket basket = Hawk.get(Constants.BASKET_KEY);
        return basket.total == 0;
    }
}
