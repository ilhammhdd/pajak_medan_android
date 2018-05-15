package com.pajakmedan.pajakmedan.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.listeners.SetOnRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by milha on 3/21/2018.
 */

public class PostDeleteAddress extends AsyncTask<JSONObject, Double, Boolean> implements SetOnRequestListener {
    OnRequestListener onRequestListener;

    private String url = Constants.DOMAIN + "api/post-delete-address";
    private String token;

    public PostDeleteAddress(String token) {
        this.token = token;
    }

    @Override
    protected Boolean doInBackground(JSONObject... jsonObjects) {
        JSONObject responseAll = RequestPost.sendRequest(this.url, jsonObjects[0], Constants.CONTENT_TYPE, this.token);
        assert responseAll != null;
        try {
            return responseAll.getBoolean("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Boolean object) {
        try {
            onRequestListener.onRequest(object, Constants.RESPONSE_DATA_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setOnRequestListener(OnRequestListener listener) {
        onRequestListener = listener;
    }
}
