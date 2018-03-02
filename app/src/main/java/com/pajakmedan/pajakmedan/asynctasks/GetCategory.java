package com.pajakmedan.pajakmedan.asynctasks;

import android.os.AsyncTask;
import android.util.Log;
import android.webkit.URLUtil;

import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.listeners.SetOnRequestListener;
import com.pajakmedan.pajakmedan.models.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by milha on 2/13/2018.
 */

public class GetCategory extends AsyncTask<JSONObject, Void, List<Category>> {

    public interface OnRequestListener {
        void onRequest(List<Category> categories) throws JSONException;
    }

    public GetCategory.OnRequestListener listener;

    public void setOnRequestListener(GetCategory.OnRequestListener listener) {
        this.listener = listener;
    }

    @Override
    protected List<Category> doInBackground(JSONObject... jsonObjects) {
        try {
            JSONObject response = RequestPost.sendRequest(jsonObjects[0]);
            assert response != null;
            JSONArray arrayResponse = response.getJSONArray("categories");

            List<Category> categories = new ArrayList<>();

            for (int i = 0; i < arrayResponse.length(); i++) {
                categories.add(new Category(arrayResponse.getJSONObject(i).getInt("id"), arrayResponse.getJSONObject(i).getString("file_path"), arrayResponse.getJSONObject(i).getString("name")));
            }

            return categories;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Category> categories) {
        try {
            if (categories != null) {
                listener.onRequest(categories);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
