package com.pajakmedan.pajakmedan.requests;

import com.pajakmedan.pajakmedan.listeners.ExecuteAsyncTaskListener;
import com.pajakmedan.pajakmedan.listeners.SetExecutedAsyncTaskListener;

abstract class BaseRequest implements SetExecutedAsyncTaskListener{
    public ExecuteAsyncTaskListener executeAsyncTaskListener;

    public void setListener(ExecuteAsyncTaskListener listener) {
        this.executeAsyncTaskListener = listener;
    }
}
