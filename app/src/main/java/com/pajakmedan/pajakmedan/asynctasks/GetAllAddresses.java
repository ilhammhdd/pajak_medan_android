package com.pajakmedan.pajakmedan.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.listeners.SetOnRequestListener;
import com.pajakmedan.pajakmedan.models.Address;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milha on 3/21/2018.
 */

public class GetAllAddresses extends AsyncTask<JSONObject, Double, List<Address>> implements SetOnRequestListener {

    OnRequestListener onRequestListener;

    @Override
    protected List<Address> doInBackground(JSONObject... jsonObjects) {
        JSONObject response = RequestPost.sendRequest(jsonObjects[0]);
        assert response != null;
        Log.d("ALL_ADDRESS_RESPONSE", response.toString());
        try {
            if (response.getJSONObject(Constants.RESPONSE_DATA_KEY).has("all_addresses")) {
                JSONArray addressesJsonArray = response.getJSONObject(Constants.RESPONSE_DATA_KEY).getJSONArray("all_addresses");
                for (int i = 0; i < addressesJsonArray.length(); i++) {
                    Address.saveAddressToList(Address.extractTheAddress(addressesJsonArray.getJSONObject(i)));
                }
            }
            return (List<Address>) Hawk.get(Constants.ALL_ADDRESS_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Address> addresses) {
        try {
            onRequestListener.onRequest(addresses, Constants.RESPONSE_DATA_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setOnRequestListener(OnRequestListener listener) {
        onRequestListener = listener;
    }
}
