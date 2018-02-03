package com.pajakmedan.pajakmedan.listeners;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by milha on 1/27/2018.
 */

public interface OnPostListener {

    void onPost(JSONObject jsonObject) throws JSONException;
}
