package com.pajakmedan.pajakmedan.asynctasks;

import android.os.AsyncTask;
import android.util.Log;
import android.webkit.URLUtil;

import com.pajakmedan.pajakmedan.models.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by milha on 2/15/2018.
 */

public class GetEvent extends AsyncTask<JSONObject, Void, HashMap<String, String>> {

    public OnRequestListener onRequestListener;

    public interface OnRequestListener {
        void onRequest(HashMap<String, String> events) throws JSONException;
    }

    public void setOnRequestListener(GetEvent.OnRequestListener onRequestListener) {
        this.onRequestListener = onRequestListener;
    }

    @Override
    protected HashMap<String, String> doInBackground(JSONObject... jsonObjects) {
        try {
            JSONObject response = RequestPost.sendRequest(jsonObjects[0]);
            assert response != null;
            JSONArray eventsJSON = response.getJSONArray("events");

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
                onRequestListener.onRequest(events);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
