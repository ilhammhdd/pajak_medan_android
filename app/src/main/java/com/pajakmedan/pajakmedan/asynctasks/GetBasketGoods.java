package com.pajakmedan.pajakmedan.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.pajakmedan.pajakmedan.models.Basket;
import com.pajakmedan.pajakmedan.models.BasketGoods;
import com.pajakmedan.pajakmedan.models.Goods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by milha on 3/1/2018.
 */

public class GetBasketGoods extends AsyncTask<JSONObject, Void, List<BasketGoods>> {

    private GetBasketGoods.OnRequestListener onRequestListener;

    public interface OnRequestListener {
        void onRequest(List<BasketGoods> basketGoods) throws JSONException;
    }

    public void setOnRequestListener(GetBasketGoods.OnRequestListener listener) {
        onRequestListener = listener;
    }

    @Override
    protected List<BasketGoods> doInBackground(JSONObject... jsonObjects) {
        List<BasketGoods> basketGoodsList = new ArrayList<>();
        JSONObject response = RequestPost.sendRequest(jsonObjects[0]);
        assert response != null;
        try {
            JSONArray basketGoods = response.getJSONArray("basket_goods");
            for (int i = 0; i < basketGoods.length(); i++) {
                Log.d("GET_BASKET_GOODS", response.getJSONArray("basket_goods").getJSONObject(i).toString());
                basketGoodsList.add(
                        new BasketGoods(
                                basketGoods.getJSONObject(i).getInt("basket_goods_id"),
                                new Goods(
                                        basketGoods.getJSONObject(i).getInt("goods_id"),
                                        basketGoods.getJSONObject(i).getString("goods_image_url"),
                                        basketGoods.getJSONObject(i).getString("goods_name"),
                                        basketGoods.getJSONObject(i).getInt("goods_price"),
                                        basketGoods.getJSONObject(i).getString("goods_unit"),
                                        basketGoods.getJSONObject(i).getInt("goods_available") == 1,
                                        basketGoods.getJSONObject(i).getInt("goods_min_order"),
                                        basketGoods.getJSONObject(i).getString("goods_condition")
                                ),
                                new Basket(
                                        basketGoods.getJSONObject(i).getInt("baskets_id"),
                                        basketGoods.getJSONObject(i).getInt("baskets_customer_id"),
                                        basketGoods.getJSONObject(i).getInt("baskets_total"),
                                        basketGoods.getJSONObject(i).getString("baskets_description"),
                                        basketGoods.getJSONObject(i).getString("basket_status_name")
                                ),
                                basketGoods.getJSONObject(i).getInt("baskets_goods_quantity"),
                                basketGoods.getJSONObject(i).getInt("baskets_goods_total_price")
                        )
                );
            }
            return basketGoodsList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<BasketGoods> goods) {
        try {
            if (goods != null) {
                onRequestListener.onRequest(goods);
            }
        } catch (JSONException j) {
            j.printStackTrace();
        }
    }
}
