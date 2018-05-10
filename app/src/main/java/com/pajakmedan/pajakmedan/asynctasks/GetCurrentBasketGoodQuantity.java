package com.pajakmedan.pajakmedan.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.listeners.SetOnRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by milha on 3/10/2018.
 */

public class GetCurrentBasketGoodQuantity extends AsyncTask<JSONObject, Void, JSONObject> implements SetOnRequestListener {
    public OnRequestListener onRequestListener;

    @Override
    protected JSONObject doInBackground(JSONObject... jsonObjects) {
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject response) {
        try {
            assert response != null;
            onRequestListener.onRequest(response, Constants.RESPONSE_DATA_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setOnRequestListener(OnRequestListener listener) {
        this.onRequestListener = listener;
    }
}
