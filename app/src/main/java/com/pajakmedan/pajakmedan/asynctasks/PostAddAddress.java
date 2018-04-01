package com.pajakmedan.pajakmedan.asynctasks;

import android.os.AsyncTask;

import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.listeners.SetOnRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by milha on 3/25/2018.
 */

public class PostAddAddress extends AsyncTask<JSONObject, Double, Boolean> implements SetOnRequestListener {
    OnRequestListener onRequestListener;

    @Override
    protected Boolean doInBackground(JSONObject... jsonObjects) {
        JSONObject responseAll = RequestPost.sendRequest(jsonObjects[0]);
        assert responseAll != null;
        try {
            JSONObject responseData = responseAll.getJSONObject(Constants.RESPONSE_DATA_KEY);
            return responseData.getBoolean("add_success");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        try {
            onRequestListener.onRequest(aBoolean, Constants.RESPONSE_DATA_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setOnRequestListener(OnRequestListener listener) {
        onRequestListener = listener;
    }
}
