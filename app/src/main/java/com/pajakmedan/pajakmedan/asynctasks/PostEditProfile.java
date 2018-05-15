package com.pajakmedan.pajakmedan.asynctasks;

import android.os.AsyncTask;

import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.listeners.SetOnRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class PostEditProfile extends AsyncTask<JSONObject, Double, Boolean> implements SetOnRequestListener {

    public String url = Constants.DOMAIN + "api/post-edit-profile";
    public String token;

    public PostEditProfile(String token) {
        this.token = token;
    }

    public static OnRequestListener listener;

    @Override
    protected Boolean doInBackground(JSONObject... jsonObjects) {
        JSONObject response = RequestPost.sendRequest(this.url, jsonObjects[0], Constants.CONTENT_TYPE, this.token);
        assert response != null;
        try {
            return response.getBoolean("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setOnRequestListener(OnRequestListener listener) {
        PostEditProfile.listener = listener;
    }
}
