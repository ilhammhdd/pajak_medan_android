package com.pajakmedan.pajakmedan.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.listeners.SetOnRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.CopyOnWriteArrayList;

public class Login extends AsyncTask<JSONObject, Void, JSONObject> implements SetOnRequestListener {
    private OnRequestListener listener;
    private String url;

    public Login(boolean alternativeLogin) {
        this.url = alternativeLogin ? Constants.DOMAIN + "api/alternative-login" : Constants.DOMAIN + "api/login";
    }

    @Override
    public void setOnRequestListener(OnRequestListener listener) {
        this.listener = listener;
    }

    @Override
    protected JSONObject doInBackground(JSONObject... jsonObjects) {
        return RequestPost.sendRequest(this.url, jsonObjects[0], Constants.CONTENT_TYPE, "");
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
