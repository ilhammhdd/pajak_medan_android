package com.pajakmedan.pajakmedan.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

/**
 * Created by milha on 3/30/2018.
 */

public class PostIssueCheckout extends AsyncTask<JSONObject, Double, Void> {
    @Override
    protected Void doInBackground(JSONObject... jsonObjects) {
        RequestPost.sendRequest(jsonObjects[0]);
        return null;
    }
}
