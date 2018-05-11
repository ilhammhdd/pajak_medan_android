package com.pajakmedan.pajakmedan.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.listeners.SetOnRequestListener;
import com.pajakmedan.pajakmedan.models.Payment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milha on 3/3/2018.
 */

public class GetPayment extends AsyncTask<JSONObject, Void, List<Payment>> implements SetOnRequestListener {
    List<Payment> paymentList;
    private OnRequestListener onRequestListener;

    private String url = Constants.DOMAIN + "api/get-payment-method";
    private String token;

    public GetPayment(String token) {
        this.token = token;
        this.paymentList = new ArrayList<>();
    }

    @Override
    public void setOnRequestListener(OnRequestListener listener) {
        onRequestListener = listener;
    }

    @Override
    protected List<Payment> doInBackground(JSONObject... jsonObjects) {
        try {
            JSONObject response = RequestGet.sendRequest(this.url, Constants.CONTENT_TYPE, this.token);
            assert response != null;

            if (response.getBoolean("success")) {
                JSONObject responseData = response.getJSONObject(Constants.RESPONSE_DATA_KEY);
                JSONArray payments = responseData.getJSONArray("payments");
                for (int i = 0; i < payments.length(); i++) {
                    paymentList.add(
                            new Payment(
                                    payments.getJSONObject(i).getInt("payment_id"),
                                    payments.getJSONObject(i).getString("payment_image_url"),
                                    payments.getJSONObject(i).getString("payment_name"),
                                    payments.getJSONObject(i).getString("payment_detail")
                            ));
                }
            }
            return paymentList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Payment> paymentList) {
        try {
            assert paymentList != null;
            onRequestListener.onRequest(paymentList, Constants.RESPONSE_DATA_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
