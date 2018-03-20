package com.pajakmedan.pajakmedan.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.listeners.SetOnRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by milha on 2/15/2018.
 */

public class GetEvent extends AsyncTask<JSONObject, Void, HashMap<String, String>> implements SetOnRequestListener {

    public OnRequestListener onRequestListener;

    public void setOnRequestListener(OnRequestListener onRequestListener) {
        this.onRequestListener = onRequestListener;
    }

    @Override
    protected HashMap<String, String> doInBackground(JSONObject... jsonObjects) {
        try {
            JSONObject response = RequestPost.sendRequest(jsonObjects[0]);
            assert response != null;
            JSONObject responseData = response.getJSONObject(Constants.RESPONSE_DATA_KEY);
            JSONArray eventsJSON = responseData.getJSONArray("events");

            HashMap<String, String> events = new HashMap<>();

            for (int i = 0; i < eventsJSON.length(); i++) {
                events.put(eventsJSON.getJSONObject(i).getString("name"), eventsJSON.getJSONObject(i).getString("file_path"));
            }

            return events;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(HashMap<String, String> events) {
        if (events != null) {
            try {
                onRequestListener.onRequest(events, Constants.RESPONSE_DATA_KEY);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
