package com.pajakmedan.pajakmedan.asynctasks;

import android.os.AsyncTask;
import android.webkit.URLUtil;

import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.listeners.SetOnRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by milha on 1/10/2018.
 */

public class Register extends AsyncTask<JSONObject, Void, JSONObject> implements SetOnRequestListener {

    private OnRequestListener listener;

    @Override
    public void setOnRequestListener(OnRequestListener listener) {
        this.listener = listener;
    }

    @Override
    protected JSONObject doInBackground(JSONObject... jsonObjects) {
        JSONObject data = jsonObjects[0];

        return postRegister(data);
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        try {
            if (listener != null) {
                listener.onRequest(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONObject postRegister(JSONObject dataChunk) {
        try {
            JSONObject data = dataChunk.getJSONObject("data");

            if (URLUtil.isHttpUrl(String.valueOf(data.getString("url")))) {

                HttpURLConnection connection = (HttpURLConnection) new URL(String.valueOf(data.get("url"))).openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                OutputStream os = connection.getOutputStream();
                BufferedOutputStream bos = new BufferedOutputStream(os);

                bos.write(dataChunk.toString().getBytes());
                bos.flush();
                bos.close();

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line;
                StringBuilder response = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    response.append(line);
                }

                br.close();

                return new JSONObject(response.toString());

            } else if (URLUtil.isHttpsUrl(String.valueOf(data.getJSONObject("data").getString("url")))) {

                HttpsURLConnection connection = (HttpsURLConnection) new URL(String.valueOf(data.get("url"))).openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                OutputStream os = connection.getOutputStream();
                BufferedOutputStream bos = new BufferedOutputStream(os);

                bos.write(data.toString().getBytes());
                bos.flush();
                bos.close();

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line;
                StringBuilder response = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    response.append(line);
                }

                br.close();

                return new JSONObject(response.toString());

            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
