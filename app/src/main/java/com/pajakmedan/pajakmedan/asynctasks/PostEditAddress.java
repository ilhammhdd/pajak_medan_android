package com.pajakmedan.pajakmedan.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.listeners.SetOnRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by milha on 3/24/2018.
 */

public class PostEditAddress extends AsyncTask<JSONObject, Double, JSONObject> implements SetOnRequestListener {
    OnRequestListener onRequestListener;

    @Override
    protected JSONObject doInBackground(JSONObject... jsonObjects) {
        Log.d("POSTEDITADDRESS_REQUEST", jsonObjects[0].toString());
        JSONObject response = RequestPost.sendRequest(jsonObjects[0]);
        assert response != null;
        try {
            return response.getJSONObject(Constants.RESPONSE_DATA_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        try {
            onRequestListener.onRequest(jsonObject, Constants.RESPONSE_DATA_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setOnRequestListener(OnRequestListener listener) {
        onRequestListener = listener;
    }
}
