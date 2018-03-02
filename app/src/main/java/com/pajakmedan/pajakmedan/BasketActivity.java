package com.pajakmedan.pajakmedan;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.adapters.BasketGoodsAdapter;
import com.pajakmedan.pajakmedan.asynctasks.GetBasket;
import com.pajakmedan.pajakmedan.asynctasks.GetBasketGoods;
import com.pajakmedan.pajakmedan.models.Basket;
import com.pajakmedan.pajakmedan.models.BasketGoods;
import com.pajakmedan.pajakmedan.models.Customer;
import com.pajakmedan.pajakmedan.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;

/**
 * Created by milha on 3/1/2018.
 */

public class BasketActivity extends BaseActivity implements BasketGoodsAdapter.ClickListener {
    @BindView(R.id.recyclerView_basket_basketGoods)
    RecyclerView recyclerViewBasketGoods;

    private List<BasketGoods> basketGoodsList;

    @Override
    int getContentId() {
        return R.layout.activity_basket;
    }

    @Override
    void insideOnCreate() {
        showBasketGoods();
    }

    private void showBasketGoods() {
        try {
            Basket basket = Hawk.get(Constants.BASKET_KEY);
            JSONObject request = new JSONObject();
            request.put("url", Constants.DOMAIN + "api/get-basket");
            request.put("api_token", Hawk.get(Constants.USER_API_TOKEN_KEY));
            request.put("basket_id", basket.basketId);

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
}
