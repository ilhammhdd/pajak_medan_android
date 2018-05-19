package com.pajakmedan.pajakmedan.requests;

import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.asynctasks.MyAsyncTask;
import com.pajakmedan.pajakmedan.listeners.AsyncTaskListener;

import org.json.JSONObject;

public class RegisterRequest extends BaseRequest {
    public void register(JSONObject requestData) {
        final MyAsyncTask myAsyncTask = new MyAsyncTask(Constants.DOMAIN + "api/register");
        myAsyncTask.setListener(new AsyncTaskListener() {
            @Override
            public void onPreExecute() {
                executeAsyncTaskListener.onPreExecute(myAsyncTask);
            }

            @Override
            public Object doInBackground(JSONObject... jsonObjects) {
                return RequestPost.sendRequest(myAsyncTask.url, jsonObjects[0], Constants.CONTENT_TYPE, "");

            }

            @Override
            public void onPostExecute(Object t) {
                executeAsyncTaskListener.onPostExecute(t);
            }
        });
        myAsyncTask.execute(requestData);
    }
}
