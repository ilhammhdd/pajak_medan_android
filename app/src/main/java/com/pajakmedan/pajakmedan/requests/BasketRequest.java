package com.pajakmedan.pajakmedan.requests;

import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.asynctasks.MyAsyncTask;
import com.pajakmedan.pajakmedan.listeners.AsyncTaskListener;
import com.pajakmedan.pajakmedan.models.Basket;
import com.pajakmedan.pajakmedan.models.BasketGoods;
import com.pajakmedan.pajakmedan.models.Goods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BasketRequest extends BaseRequest {

    public void getBasket() {
        final MyAsyncTask requestMyAsyncTask = new MyAsyncTask(Constants.DOMAIN + "api/get-basket", Constants.getUserToken());
        requestMyAsyncTask.setListener(new AsyncTaskListener() {
            @Override
            public void onPreExecute() {
                executeAsyncTaskListener.onPreExecute(requestMyAsyncTask);
            }

            @Override
            public Object doInBackground(JSONObject... jsonObjects) {
                return RequestGet.sendRequest(requestMyAsyncTask.url, Constants.CONTENT_TYPE, requestMyAsyncTask.token);
            }

            @Override
            public void onPostExecute(Object t) {
                executeAsyncTaskListener.onPostExecute(t);
            }
        });
        requestMyAsyncTask.execute();
    }

    public void getBasketGoods(JSONObject requestData) {
        final MyAsyncTask requestMyAsyncTask = new MyAsyncTask(Constants.DOMAIN + "api/get-basket-goods", Constants.getUserToken());
        requestMyAsyncTask.setListener(new AsyncTaskListener() {
            @Override
            public Object doInBackground(JSONObject... jsonObjects) {
                List<BasketGoods> basketGoodsList = new ArrayList<>();
                JSONObject response = RequestPost.sendRequest(requestMyAsyncTask.url, jsonObjects[0], Constants.CONTENT_TYPE, requestMyAsyncTask.token);
                assert response != null;
                try {
                    JSONObject responseData = response.getJSONObject(Constants.RESPONSE_DATA_KEY);
                    JSONArray basketGoods = responseData.getJSONArray("basket_goods");
                    for (int i = 0; i < basketGoods.length(); i++) {
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
            public void onPostExecute(Object t) {
                executeAsyncTaskListener.onPostExecute(t);
            }

            @Override
            public void onProgressUpdate(Double... values) {
                executeAsyncTaskListener.onProgressUpdate(values);
            }

            @Override
            public void onPreExecute() {
                executeAsyncTaskListener.onPreExecute(requestMyAsyncTask);
            }
        });
        requestMyAsyncTask.execute(requestData);
    }
}
