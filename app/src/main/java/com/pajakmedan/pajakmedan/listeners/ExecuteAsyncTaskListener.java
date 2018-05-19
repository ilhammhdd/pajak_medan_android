package com.pajakmedan.pajakmedan.listeners;

import com.pajakmedan.pajakmedan.asynctasks.MyAsyncTask;

public abstract class ExecuteAsyncTaskListener {

    public void onPreExecute(MyAsyncTask myAsyncTask) {
    }

    public void onPostExecute(Object t) {
    }

    public void onProgressUpdate(Double... values) {
    }

    public void onCancelled(Object t) {
    }

    public void onCancelled() {
    }
}
