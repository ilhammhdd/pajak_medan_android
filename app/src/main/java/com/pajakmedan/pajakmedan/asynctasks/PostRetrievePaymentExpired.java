package com.pajakmedan.pajakmedan.asynctasks;

import android.os.AsyncTask;

import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.listeners.SetOnRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class PostRetrievePaymentExpired extends AsyncTask<JSONObject, Double, JSONObject> implements SetOnRequestListener {

    private String url = Constants.DOMAIN + "api/get-payment-expired";
    private String token;

    public PostRetrievePaymentExpired(String token) {
        this.token = token;
    }

    public static OnRequestListener listener;

    @Override
    protected JSONObject doInBackground(JSONObject... jsonObjects) {
        return RequestPost.sendRequest(this.url, jsonObjects[0], Constants.CONTENT_TYPE, this.token);
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        try {
            listener.onRequest(jsonObject, Constants.RESPONSE_DATA_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setOnRequestListener(OnRequestListener listener) {
        PostRetrievePaymentExpired.listener = listener;
    }
}
