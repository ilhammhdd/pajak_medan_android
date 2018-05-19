package com.pajakmedan.pajakmedan;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.adapters.OrderGoodsAdapter;
import com.pajakmedan.pajakmedan.asynctasks.MyAsyncTask;
import com.pajakmedan.pajakmedan.listeners.ExecuteAsyncTaskListener;
import com.pajakmedan.pajakmedan.models.BasketGoods;
import com.pajakmedan.pajakmedan.requests.CheckoutRequest;

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
        CheckoutRequest checkoutRequest = new CheckoutRequest();
        checkoutRequest.setListener(new ExecuteAsyncTaskListener() {
            @Override
            public void onPreExecute(MyAsyncTask myAsyncTask) {
                myAsyncTaskList.add(myAsyncTask);
            }
            @Override
            public void onPostExecute(Object t) {
                basketGoodsList = (List<BasketGoods>) t;
                OrderGoodsAdapter orderGoodsAdapter = new OrderGoodsAdapter(OrderGoodsActivity.this, basketGoodsList);
                recyclerViewOrderGoods.setLayoutManager(new LinearLayoutManager(OrderGoodsActivity.this, LinearLayoutManager.VERTICAL, false));
                recyclerViewOrderGoods.setAdapter(orderGoodsAdapter);
            }
        });
        try {
            checkoutRequest.getCheckoutGoods(new JSONObject()
                    .put("checkout_id", Hawk.get(Constants.CURRENT_CHECKOUT_ID_KEY)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.imageView_orderGoods_back)
    void backPressed() {
        finish();
    }
}
