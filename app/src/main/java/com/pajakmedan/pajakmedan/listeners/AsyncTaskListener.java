package com.pajakmedan.pajakmedan.listeners;

import org.json.JSONObject;

public abstract class AsyncTaskListener {
    public abstract Object doInBackground(JSONObject... jsonObjects);

    public void onPostExecute(Object t){
    }

    public void onProgressUpdate(Double... values){
    }

    public void onPreExecute() {
    }

    public void onCancelled(Object t){
    }

    public void onCancelled() {
    }
}
