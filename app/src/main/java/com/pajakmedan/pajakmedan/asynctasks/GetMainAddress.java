package com.pajakmedan.pajakmedan.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.listeners.SetOnRequestListener;
import com.pajakmedan.pajakmedan.models.Address;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by milha on 3/19/2018.
 */

public class GetMainAddress extends AsyncTask<JSONObject, Double, JSONObject> implements SetOnRequestListener {

    private OnRequestListener onRequestListener;

    @Override
    protected JSONObject doInBackground(JSONObject... jsonObjects) {
        try {
            Log.d("LOGGING_MAIN_ADDRESS", "REQUEST : "+String.valueOf(jsonObjects[0]));
            JSONObject response = RequestPost.sendRequest(jsonObjects[0]);
            assert response != null;
            if (response.getJSONObject(Constants.RESPONSE_DATA_KEY).has("main_address")) {
                return response.getJSONObject(Constants.RESPONSE_DATA_KEY).getJSONObject("main_address");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject object) {
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
