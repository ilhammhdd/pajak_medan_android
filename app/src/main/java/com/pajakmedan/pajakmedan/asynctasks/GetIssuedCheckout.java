package com.pajakmedan.pajakmedan.asynctasks;

import android.os.AsyncTask;

import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.listeners.SetOnRequestListener;
import com.pajakmedan.pajakmedan.models.Checkout;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by milha on 3/30/2018.
 */

public class GetIssuedCheckout extends AsyncTask<JSONObject, Double, List<Checkout>> implements SetOnRequestListener {
    OnRequestListener listener;

    @Override
    protected List<Checkout> doInBackground(JSONObject... jsonObjects) {
        JSONObject response = RequestPost.sendRequest(jsonObjects[0]);
        
        return null;
    }

    @Override
    public void setOnRequestListener(OnRequestListener listener) {
        this.listener = listener;
    }
}
