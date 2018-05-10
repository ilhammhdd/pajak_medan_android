package com.pajakmedan.pajakmedan.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.listeners.SetOnRequestListener;
import com.pajakmedan.pajakmedan.models.BasketGoods;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by milha on 4/6/2018.
 */

public class GetCheckoutBasketGoods extends AsyncTask<JSONObject, Double, List<BasketGoods>> implements SetOnRequestListener {
    OnRequestListener onRequestListener;

    private String url = Constants.DOMAIN + "api/get-checkout-basket-goods";
    private String token;

    public GetCheckoutBasketGoods(String token) {
        this.token = token;
    }

    @Override
    protected List<BasketGoods> doInBackground(JSONObject... jsonObjects) {
        JSONObject response = RequestPost.sendRequest(this.url, jsonObjects[0], Constants.CONTENT_TYPE, this.token);
        assert response != null;
        try {
            return BasketGoods.getCheckoutBasketGoods(response.getJSONObject(Constants.RESPONSE_DATA_KEY).getJSONArray("checkout_basket_goods"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<BasketGoods> basketGoods) {
        try {
            onRequestListener.onRequest(basketGoods, Constants.RESPONSE_DATA_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setOnRequestListener(OnRequestListener listener) {
        this.onRequestListener = listener;
    }
}
