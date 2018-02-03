package com.pajakmedan.pajakmedan.asynctasks;

import android.os.AsyncTask;
import android.webkit.URLUtil;

import com.pajakmedan.pajakmedan.listeners.OnPostListener;
import com.pajakmedan.pajakmedan.listeners.SetOnPostListener;

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

public class Login extends AsyncTask<JSONObject, Void, JSONObject> implements SetOnPostListener {
    private OnPostListener listener;

    @Override
    public void setOnPostListener(com.pajakmedan.pajakmedan.listeners.OnPostListener listener) {
        this.listener = listener;
    }

    @Override
    protected JSONObject doInBackground(JSONObject... jsonObjects) {
        try {
            JSONObject temp = jsonObjects[0].getJSONObject("data");
            String url = temp.getString("url");

            if (URLUtil.isHttpUrl(url)) {
                return httpPostLogin(jsonObjects);
            } else if (URLUtil.isHttpsUrl(url)) {
                return httpsPostLogin(jsonObjects);
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        try {
            if (listener != null) {
                listener.onPost(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONObject httpPostLogin(JSONObject... jsonObjects) throws JSONException, IOException {
        JSONObject temp = jsonObjects[0].getJSONObject("data");
        String url = temp.getString("url");

        URL urlLogin = new URL(url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) urlLogin.openConnection();

        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);

        OutputStream outputStream = httpURLConnection.getOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
        bufferedOutputStream.write(jsonObjects[0].toString().getBytes());
        bufferedOutputStream.flush();

        bufferedOutputStream.close();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

        String line = "";
        StringBuilder sb = new StringBuilder();

        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }

        bufferedReader.close();

        return new JSONObject(sb.toString());
    }

    private JSONObject httpsPostLogin(JSONObject... jsonObjects) throws JSONException, IOException {
        JSONObject temp = jsonObjects[0];
        String url = temp.getString("url");

        URL urlLogin = new URL(url);
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) urlLogin.openConnection();

        httpsURLConnection.setRequestMethod("POST");
        httpsURLConnection.setRequestProperty("Content-Type", "application/json");
        httpsURLConnection.setDoOutput(true);

        OutputStream outputStream = httpsURLConnection.getOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
        bufferedOutputStream.write(jsonObjects[0].toString().getBytes());
        bufferedOutputStream.flush();
        bufferedOutputStream.close();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));

        String line = "";
        StringBuilder sb = new StringBuilder();

        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }

        bufferedReader.close();

        return new JSONObject(sb.toString());
    }
}
