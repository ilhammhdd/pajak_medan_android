package com.pajakmedan.pajakmedan.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.listeners.SetOnRequestListener;
import com.pajakmedan.pajakmedan.models.Checkout;
import com.pajakmedan.pajakmedan.models.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milha on 3/30/2018.
 */

public class GetIssuedCheckout extends AsyncTask<JSONObject, Double, List<Order>> implements SetOnRequestListener {

    List<Order> orderList;
    OnRequestListener listener;

    @Override
    protected List<Order> doInBackground(JSONObject... jsonObjects) {
        JSONObject response = RequestPost.sendRequest(jsonObjects[0]);
        orderList = new ArrayList<>();
        try {
            if (response != null) {
                JSONArray jsonArray = response.getJSONObject(Constants.RESPONSE_DATA_KEY).getJSONArray("orders");
                for (int i = 0; i < jsonArray.length(); i++) {
                    orderList.add(
                            Order.getAndSaveOrderFromJSON(
                                    jsonArray.getJSONObject(i)
                            ));
                }
                return orderList;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Order> orders) {
        try {
            this.listener.onRequest(orders, Constants.RESPONSE_DATA_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setOnRequestListener(OnRequestListener listener) {
        this.listener = listener;
    }
}
