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

    @Override
    public void setOnRequestListener(OnRequestListener listener) {
        onRequestListener = listener;
    }

    @Override
    protected JSONObject doInBackground(JSONObject... jsonObjects) {
        return RequestPost.sendRequest(jsonObjects[0]);
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
