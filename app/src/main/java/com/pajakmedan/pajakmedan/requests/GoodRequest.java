package com.pajakmedan.pajakmedan.requests;

import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.asynctasks.MyAsyncTask;
import com.pajakmedan.pajakmedan.listeners.AsyncTaskListener;
import com.pajakmedan.pajakmedan.models.Category;
import com.pajakmedan.pajakmedan.models.Goods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GoodRequest extends BaseRequest {
    public void getGoods(Category category) {
        final MyAsyncTask myAsyncTask = new MyAsyncTask(Constants.DOMAIN + "api/get-goods", Constants.getUserToken());
        myAsyncTask.setListener(new AsyncTaskListener() {
            @Override
            public void onPreExecute() {
                executeAsyncTaskListener.onPreExecute(myAsyncTask);
            }

            @Override
            public Object doInBackground(JSONObject... jsonObjects) {
                try {
                    List<Goods> goodsList = new ArrayList<>();
                    JSONObject response = RequestPost.sendRequest(myAsyncTask.url, jsonObjects[0], Constants.CONTENT_TYPE, myAsyncTask.token);
                    if (response != null) {
                        JSONObject responseData = response.getJSONObject(Constants.RESPONSE_DATA_KEY);
                        JSONArray jsonArrayGoods = responseData.getJSONArray("goods");
                        for (int n = 0; n < jsonArrayGoods.length(); n++) {
                            goodsList.add(
                                    new Goods(
                                            jsonArrayGoods.getJSONObject(n).getInt("id"),
                                            jsonArrayGoods.getJSONObject(n).getString("file_path"),
                                            jsonArrayGoods.getJSONObject(n).getString("name"),
                                            jsonArrayGoods.getJSONObject(n).getInt("price"),
                                            jsonArrayGoods.getJSONObject(n).getString("unit"),
                                            jsonArrayGoods.getJSONObject(n).getInt("available") == 1,
                                            jsonArrayGoods.getJSONObject(n).getInt("min_order"),
                                            jsonArrayGoods.getJSONObject(n).getString("condition")
                                    )
                            );
                        }

                        return goodsList;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void onPostExecute(Object t) {
                executeAsyncTaskListener.onPostExecute(t);
            }
        });

        try {
            myAsyncTask.execute(new JSONObject().put("category_id", category.categoryId));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void buyGoods(JSONObject requestData) {
        final MyAsyncTask myAsyncTask = new MyAsyncTask(Constants.DOMAIN + "api/buy-goods", Constants.getUserToken());
        myAsyncTask.setListener(new AsyncTaskListener() {
            @Override
            public void onPreExecute() {
                executeAsyncTaskListener.onPreExecute(myAsyncTask);
            }

            @Override
            public Object doInBackground(JSONObject... jsonObjects) {
                return RequestPost.sendRequest(myAsyncTask.url, jsonObjects[0], Constants.CONTENT_TYPE, myAsyncTask.token);
            }

            @Override
            public void onPostExecute(Object t) {
                executeAsyncTaskListener.onPostExecute(t);
            }
        });
        myAsyncTask.execute(requestData);
    }
}
