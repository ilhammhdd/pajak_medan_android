package com.pajakmedan.pajakmedan.requests;

import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.asynctasks.MyAsyncTask;
import com.pajakmedan.pajakmedan.listeners.AsyncTaskListener;
import com.pajakmedan.pajakmedan.models.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CategoryRequest extends BaseRequest {

    public void getAllCateogies() {
        final MyAsyncTask myAsyncTask = new MyAsyncTask(Constants.DOMAIN + "api/get-categories", Constants.getUserToken());
        myAsyncTask.setListener(new AsyncTaskListener() {
            @Override
            public void onPreExecute() {
                executeAsyncTaskListener.onPreExecute(myAsyncTask);
            }

            @Override
            public Object doInBackground(JSONObject... jsonObjects) {
                try {
                    JSONObject response = RequestGet.sendRequest(myAsyncTask.url, Constants.CONTENT_TYPE, myAsyncTask.token);
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
            public void onPostExecute(Object t) {
                executeAsyncTaskListener.onPostExecute(t);
            }
        });

        myAsyncTask.execute();
    }
}
