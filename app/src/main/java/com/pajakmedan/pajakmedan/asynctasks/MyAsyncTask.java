package com.pajakmedan.pajakmedan.asynctasks;

import com.pajakmedan.pajakmedan.listeners.AsyncTaskListener;
import com.pajakmedan.pajakmedan.listeners.SetAsyncTaskListener;

import org.json.JSONObject;

public class MyAsyncTask extends android.os.AsyncTask<JSONObject, Double, Object> implements SetAsyncTaskListener {

    private AsyncTaskListener asyncTaskListener;
    public String url;
    public String token;

    public MyAsyncTask(){}

    public MyAsyncTask(String url) {
        this.url = url;
    }

    public MyAsyncTask(String url, String token) {
        this.url = url;
        this.token = token;
    }

    @Override
    protected Object doInBackground(JSONObject... jsonObjects) {
        return this.asyncTaskListener.doInBackground(jsonObjects);
    }

    @Override
    public void setListener(AsyncTaskListener asyncTaskListener) {
        this.asyncTaskListener = asyncTaskListener;
    }

    protected void onPostExecute(Object t) {
        this.asyncTaskListener.onPostExecute(t);
    }

    protected void onProgressUpdate(Double... values) {
        this.asyncTaskListener.onProgressUpdate(values);
    }

    protected void onPreExecute() {
        this.asyncTaskListener.onPreExecute();
    }

    protected void onCancelled(Object t) {
        this.asyncTaskListener.onCancelled(t);
    }

    protected void onCancelled() {
        this.asyncTaskListener.onCancelled();
    }
}
