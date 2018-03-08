package com.pajakmedan.pajakmedan;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.adapters.BasketGoodsAdapter;
import com.pajakmedan.pajakmedan.asynctasks.GetBasketGoods;
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
    CardView cardViewTest;

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

            getBasketGoods.setOnRequestListener(new GetBasketGoods.OnRequestListener() {
                @Override
                public void onRequest(List<BasketGoods> basketGoods) throws JSONException {
                    basketGoodsList = basketGoods;
                    BasketGoodsAdapter basketGoodsAdapter = new BasketGoodsAdapter(BasketActivity.this, basketGoods);
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

    @OnClick(R.id.textView_expand)
    void expand() {
//        Log.d("GET_THIS", "dapatnyaaa");
        Log.d("GET_THIS", "Layout params height : " + cardViewTest.getLayoutParams().height);
        Log.d("GET_THIS", "Layout params width : " + cardViewTest.getLayoutParams().width);
        Log.d("GET_THIS", "Device height : " + getDeviceHeight());
        Log.d("GET_THIS", "Device width : " + getDeviceWidth());
        if (!clicked) {
            cardViewTest.setLayoutParams(new ConstraintLayout.LayoutParams(getDeviceWidth(), getDeviceHeight() - getDeviceHeight() / 8));
            clicked = true;
            return;
        }
        cardViewTest.setLayoutParams(new ConstraintLayout.LayoutParams(getDeviceWidth(), (int) (getDeviceHeight() / 2.5d)));
        clicked = false;
    }
}
