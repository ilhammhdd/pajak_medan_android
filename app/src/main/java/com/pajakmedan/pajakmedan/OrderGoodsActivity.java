package com.pajakmedan.pajakmedan;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.adapters.BasketGoodsAdapter;
import com.pajakmedan.pajakmedan.adapters.OrderGoodsAdapter;
import com.pajakmedan.pajakmedan.asynctasks.GetBasketGoods;
import com.pajakmedan.pajakmedan.asynctasks.GetCheckoutBasketGoods;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.models.Basket;
import com.pajakmedan.pajakmedan.models.BasketGoods;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by milha on 4/6/2018.
 */

public class OrderGoodsActivity extends BaseActivity {

    @BindView(R.id.recyclerView_orderGoods)
    RecyclerView recyclerViewOrderGoods;

    List<BasketGoods> basketGoodsList;

    @Override
    int getContentId() {
        return R.layout.activity_order_goods;
    }

    @Override
    void insideOnCreate() {
        showCheckoutBasketGoods();
    }

    private void showCheckoutBasketGoods() {
        try {
            GetCheckoutBasketGoods getCheckoutBasketGoods = new GetCheckoutBasketGoods(String.valueOf(Hawk.get(Constants.USER_API_TOKEN_KEY)));
            getCheckoutBasketGoods.execute(new JSONObject()
                    .put("checkout_id", Hawk.get(Constants.CURRENT_CHECKOUT_ID_KEY))
            );

            getCheckoutBasketGoods.setOnRequestListener(new OnRequestListener() {
                @Override
                public <T> void onRequest(T basketGoods, String key) throws JSONException {
                    basketGoodsList = (List<BasketGoods>) basketGoods;
                    OrderGoodsAdapter orderGoodsAdapter = new OrderGoodsAdapter(OrderGoodsActivity.this, basketGoodsList);
                    recyclerViewOrderGoods.setLayoutManager(new LinearLayoutManager(OrderGoodsActivity.this, LinearLayoutManager.VERTICAL, false));
                    recyclerViewOrderGoods.setAdapter(orderGoodsAdapter);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.imageView_orderGoods_back)
    void backPressed() {
        finish();
    }
}
