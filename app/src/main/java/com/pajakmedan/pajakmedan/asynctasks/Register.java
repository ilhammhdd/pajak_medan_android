package com.pajakmedan.pajakmedan.asynctasks;

import android.os.AsyncTask;
import android.webkit.URLUtil;

import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.listeners.SetOnRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by milha on 1/10/2018.
 */

public class Register extends AsyncTask<JSONObject, Void, JSONObject> implements SetOnRequestListener {

    private OnRequestListener listener;
    private String url = Constants.DOMAIN + "api/register";

    @Override
    public void setOnRequestListener(OnRequestListener listener) {
        this.listener = listener;
    }

    @Override
    protected JSONObject doInBackground(JSONObject... jsonObjects) {
        return RequestPost.sendRequest(url, jsonObjects[0], Constants.CONTENT_TYPE, "");
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
