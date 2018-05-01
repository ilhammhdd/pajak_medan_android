package com.pajakmedan.pajakmedan.asynctasks;

import android.os.AsyncTask;

import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.listeners.SetOnRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by milha on 3/30/2018.
 */

public class PostIssueCheckout extends AsyncTask<JSONObject, Double, JSONObject> implements SetOnRequestListener {

    private static OnRequestListener onRequestListener;

    private String url = Constants.DOMAIN + "api/post-issue-checkout";
    private String token;

    public PostIssueCheckout(String token) {
        this.token = token;
    }

    @Override
    protected JSONObject doInBackground(JSONObject... jsonObjects) {
        RequestPost.sendRequest(this.url, jsonObjects[0], Constants.CONTENT_TYPE, this.token);
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        try {
            onRequestListener.onRequest(jsonObject,Constants.RESPONSE_DATA_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setOnRequestListener(OnRequestListener listener) {
        PostIssueCheckout.onRequestListener = listener;
    }
}
