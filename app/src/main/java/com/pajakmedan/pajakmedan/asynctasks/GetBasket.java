package com.pajakmedan.pajakmedan.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.listeners.SetOnRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by milha on 3/1/2018.
 */

public class GetBasket extends AsyncTask<JSONObject, Void, JSONObject> implements SetOnRequestListener {

    public OnRequestListener onRequestListener;
    private String url = Constants.DOMAIN + "api/get-basket";
    private String token;

    public GetBasket(String token) {
        this.token = token;
    }

    @Override
    public void setOnRequestListener(OnRequestListener listener) {
        onRequestListener = listener;
    }

    @Override
    protected JSONObject doInBackground(JSONObject... jsonObjects) {
        return RequestGet.sendRequest(this.url, Constants.CONTENT_TYPE, this.token);
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        try {
            if (jsonObject != null) {
                onRequestListener.onRequest(jsonObject, "response_data");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
