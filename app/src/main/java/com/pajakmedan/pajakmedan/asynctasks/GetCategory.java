package com.pajakmedan.pajakmedan.asynctasks;

import android.os.AsyncTask;

import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.listeners.SetOnRequestListener;
import com.pajakmedan.pajakmedan.models.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milha on 2/13/2018.
 */

public class GetCategory extends AsyncTask<JSONObject, Void, List<Category>> implements SetOnRequestListener {

    public OnRequestListener listener;
    private String token;
    private String url = Constants.DOMAIN + "api/get-categories";

    public GetCategory(String token) {
        this.token = token;
    }

    public void setOnRequestListener(OnRequestListener listener) {
        this.listener = listener;
    }

    @Override
    protected List<Category> doInBackground(JSONObject... jsonObjects) {
        try {
            JSONObject response = RequestGet.sendRequest(this.url, Constants.CONTENT_TYPE, this.token);
            assert response != null;
            JSONObject responseData = response.getJSONObject(Constants.RESPONSE_DATA_KEY);
            JSONArray arrayResponse = responseData.getJSONArray("categories");

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
                listener.onRequest(categories, Constants.RESPONSE_DATA_KEY);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
