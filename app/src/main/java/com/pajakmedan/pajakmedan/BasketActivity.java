package com.pajakmedan.pajakmedan;

import android.content.Intent;
import android.media.Image;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.adapters.BasketGoodsAdapter;
import com.pajakmedan.pajakmedan.asynctasks.GetBasketGoods;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.models.Basket;
import com.pajakmedan.pajakmedan.models.BasketGoods;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by milha on 3/1/2018.
 */

public class BasketActivity extends BaseActivity implements BasketGoodsAdapter.ClickListener {
    @BindView(R.id.recyclerView_basket_basketGoods)
    RecyclerView recyclerViewBasketGoods;
    @BindView(R.id.cardView_basket_basketGoods)
    CardView cardViewBasketGoods;
    @BindView(R.id.cardView_basket_totalPrice)
    CardView cardViewTotalPrice;
    @BindView(R.id.cardView_basket_address)
    CardView cardViewAddress;
    @BindView(R.id.cardView_basket_receiverData)
    CardView cardViewReceiverData;
    @BindView(R.id.imageView_basket_expand)
    ImageView imageViewExpand;
    @BindView(R.id.button_basket_lanjutkanPembayaran)
    Button buttonContinuePayment;
    @BindView(R.id.toolbar5)
    Toolbar toolbarBasket5;

    private List<BasketGoods> basketGoodsList;
    static BasketActivity basketActivity;
    boolean clicked = false;

    @Override
    int getContentId() {
        return R.layout.activity_basket;
    }

    @Override
    void insideOnCreate() {
        basketActivity = this;
        showBasketGoods();
        imageViewExpand.setImageLevel(1);
        setSizes();
    }

    private void setSizes() {
        cardViewBasketGoods.getLayoutParams().height = (int) (getDeviceHeight() * 0.5d);
        cardViewReceiverData.getLayoutParams().height = (int) (getDeviceHeight() * 0.25d);
        cardViewAddress.getLayoutParams().height = (int) (getDeviceHeight() * 0.25d);
//        cardViewTotalPrice.getLayoutParams().height = (int) (getDeviceHeight() * 0.2d);
    }

    private void showBasketGoods() {
        try {
            Basket basket = Hawk.get(Constants.BASKET_KEY);

            GetBasketGoods getBasketGoods = new GetBasketGoods();
            getBasketGoods.execute(new JSONObject()
                    .put("data", new JSONObject()
                            .put("url", Constants.DOMAIN + "api/get-basket-goods")
                            .put("api_token", Hawk.get(Constants.USER_API_TOKEN_KEY))
                            .put("basket_id", basket.basketId)
                    ));

            getBasketGoods.setOnRequestListener(new OnRequestListener() {
                @Override
                public <T> void onRequest(T basketGoods, String key) throws JSONException {
                    basketGoodsList = (List<BasketGoods>) basketGoods;
                    BasketGoodsAdapter basketGoodsAdapter = new BasketGoodsAdapter(BasketActivity.this, basketGoodsList);
                    basketGoodsAdapter.setClickListener(BasketActivity.this);
                    recyclerViewBasketGoods.setLayoutManager(new LinearLayoutManager(BasketActivity.this, LinearLayoutManager.VERTICAL, false));
                    recyclerViewBasketGoods.setAdapter(basketGoodsAdapter);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void clickItem(View view, int position) {
        Toast.makeText(BasketActivity.this, String.valueOf(basketGoodsList.get(position).totalPrice), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.imageView_basket_back)
    void backPressed() {
        finish();
    }

    @OnClick(R.id.button_basket_lanjutkanPembayaran)
    void lanjutPembayaran() {
        startActivity(new Intent(BasketActivity.this, PaymentActivity.class));
    }

    @OnClick(R.id.imageView_basket_expand)
    void expand() {
        if (!clicked) {
            cardViewBasketGoods.getLayoutParams().height = getDeviceHeight() - (int) (toolbarBasket5.getLayoutParams().height * 1.45);
            cardViewAddress.setVisibility(View.GONE);
            cardViewTotalPrice.setVisibility(View.GONE);
            cardViewReceiverData.setVisibility(View.GONE);
            buttonContinuePayment.setVisibility(View.GONE);
            imageViewExpand.setImageLevel(0);
            clicked = true;
            return;
        }
        cardViewBasketGoods.getLayoutParams().height = (int) (getDeviceHeight() * 0.5d);
        cardViewAddress.setVisibility(View.VISIBLE);
        cardViewTotalPrice.setVisibility(View.VISIBLE);
        cardViewReceiverData.setVisibility(View.VISIBLE);
        buttonContinuePayment.setVisibility(View.VISIBLE);
        imageViewExpand.setImageLevel(1);
        clicked = false;
    }
}
