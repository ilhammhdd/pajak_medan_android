package com.pajakmedan.pajakmedan.asynctasks;

import android.os.AsyncTask;

import com.pajakmedan.pajakmedan.models.Checkout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milha on 3/7/2018.
 */

public class GetCheckout extends AsyncTask<JSONObject, Void, List<Checkout>> {

    @Override
    protected List<Checkout> doInBackground(JSONObject... jsonObjects) {
        try {
            JSONObject response = RequestPost.sendRequest(jsonObjects[0]);
            assert response != null;
            JSONArray checkoutJSONArray = response.getJSONArray("checkouts");
            List<Checkout> checkoutList = new ArrayList<>();
            for (int i = 0; i < checkoutJSONArray .length(); i++) {
                checkoutList.add(Checkout.saveCheckout(checkoutJSONArray .getJSONObject(i)));
            }

            return checkoutList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Checkout> checkouts) {
        super.onPostExecute(checkouts);
    }
}
