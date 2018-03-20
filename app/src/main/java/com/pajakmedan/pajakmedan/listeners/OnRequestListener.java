package com.pajakmedan.pajakmedan.listeners;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by milha on 1/27/2018.
 */

public abstract class OnRequestListener {
    public abstract <T> void onRequest(T responseGeneric, String key) throws JSONException;
}
