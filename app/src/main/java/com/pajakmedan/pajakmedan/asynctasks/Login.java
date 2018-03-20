package com.pajakmedan.pajakmedan.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.listeners.SetOnRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AsyncTask<JSONObject, Void, JSONObject> implements SetOnRequestListener {
    private OnRequestListener listener;

    @Override
    public void setOnRequestListener(OnRequestListener listener) {
        this.listener = listener;
    }

    @Override
    protected JSONObject doInBackground(JSONObject... jsonObjects) {
        return RequestPost.sendRequest(jsonObjects[0]);
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        try {
            if (listener != null) {
                listener.onRequest(jsonObject, Constants.RESPONSE_DATA_KEY);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
