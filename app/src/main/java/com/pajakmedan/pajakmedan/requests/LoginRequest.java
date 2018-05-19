package com.pajakmedan.pajakmedan.requests;

import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.asynctasks.MyAsyncTask;
import com.pajakmedan.pajakmedan.listeners.AsyncTaskListener;

import org.json.JSONObject;

public class LoginRequest extends BaseRequest {
    public void login(boolean nativeLogin, JSONObject requestData) {
        String url = nativeLogin ? Constants.DOMAIN + "api/login" : Constants.DOMAIN + "api/alternative-login";
        final MyAsyncTask myAsyncTask = new MyAsyncTask(url, Constants.getUserToken());
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
