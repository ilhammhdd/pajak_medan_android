package com.pajakmedan.pajakmedan.asynctasks;

import android.os.AsyncTask;

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

    @Override
    public void setOnRequestListener(OnRequestListener listener) {
        this.listener = listener;
    }

    @Override
    protected List<Goods> doInBackground(JSONObject... jsonObjects) {
        try {
            List<Goods> goodsList = new ArrayList<>();
            JSONObject response = RequestPost.sendRequest(jsonObjects[0]);
            if (response != null) {
                JSONObject responseData = response.getJSONObject(Constants.RESPONSE_DATA_KEY);
                JSONArray jsonArrayGoods = responseData.getJSONArray("goods");
                boolean available;
                for (int n = 0; n < jsonArrayGoods.length(); n++) {
                    available = jsonArrayGoods.getJSONObject(n).getInt("available") == 1;
                    goodsList.add(
                            new Goods(
                                    jsonArrayGoods.getJSONObject(n).getInt("id"),
                                    jsonArrayGoods.getJSONObject(n).getString("file_path"),
                                    jsonArrayGoods.getJSONObject(n).getString("name"),
                                    jsonArrayGoods.getJSONObject(n).getInt("price"),
                                    jsonArrayGoods.getJSONObject(n).getString("unit"),
                                    available,
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
