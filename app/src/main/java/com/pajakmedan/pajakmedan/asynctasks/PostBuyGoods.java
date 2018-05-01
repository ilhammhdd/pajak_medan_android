package com.pajakmedan.pajakmedan.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.listeners.SetOnRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by milha on 3/11/2018.
 */

public class PostBuyGoods extends AsyncTask<JSONObject, Void, JSONObject> implements SetOnRequestListener {

    private OnRequestListener onRequestListener;
    private String url = Constants.DOMAIN + "api/buy-goods";
    private String token;

    public PostBuyGoods(String token) {
        this.token = token;
    }

    @Override
    protected JSONObject doInBackground(JSONObject... jsonObjects) {
        return RequestPost.sendRequest(this.url, jsonObjects[0], Constants.CONTENT_TYPE, this.token);
    }

    @Override
    protected void onPostExecute(JSONObject object) {
        if (object != null) {
            try {
                onRequestListener.onRequest(object, Constants.RESPONSE_DATA_KEY);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setOnRequestListener(OnRequestListener listener) {
        this.onRequestListener = listener;
    }
}
