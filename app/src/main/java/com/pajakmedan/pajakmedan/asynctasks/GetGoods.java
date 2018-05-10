package com.pajakmedan.pajakmedan.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.listeners.SetOnRequestListener;
import com.pajakmedan.pajakmedan.models.Goods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milha on 2/19/2018.
 */

public class GetGoods extends AsyncTask<JSONObject, Void, List<Goods>> implements SetOnRequestListener {

    OnRequestListener listener;
    private String url = Constants.DOMAIN + "api/get-goods";
    private String token;

    public GetGoods(String token) {
        this.token = token;
    }

    @Override
    public void setOnRequestListener(OnRequestListener listener) {
        this.listener = listener;
    }

    @Override
    protected List<Goods> doInBackground(JSONObject... jsonObjects) {
        try {
            List<Goods> goodsList = new ArrayList<>();
            JSONObject response = RequestPost.sendRequest(this.url, jsonObjects[0], Constants.CONTENT_TYPE, this.token);
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
    protected void onPostExecute(List<Goods> goodsList) {
        if (goodsList != null) {
            try {
                listener.onRequest(goodsList, Constants.RESPONSE_DATA_KEY);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
