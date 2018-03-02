package com.pajakmedan.pajakmedan.asynctasks;

import android.util.Log;
import android.webkit.URLUtil;

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
 * Created by milha on 2/19/2018.
 */

public class RequestPost {

    private static String TAG = "REQUEST_POST";

    public static JSONObject sendRequest(JSONObject dataChunk) {
        try {
            if (URLUtil.isHttpUrl(dataChunk.getJSONObject("data").getString("url"))) {
                return sendHttpRequest(dataChunk);
            } else if (URLUtil.isHttpsUrl(dataChunk.getString("url"))) {
                return sendHttpsRequest(dataChunk);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static JSONObject sendHttpRequest(JSONObject dataChunk) {
        try {
            URL url = new URL(dataChunk.getJSONObject("data").getString("url"));
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            bufferedOutputStream.write(dataChunk.toString().getBytes());
            bufferedOutputStream.flush();
            bufferedOutputStream.close();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

            String line;
            StringBuilder sb = new StringBuilder();
            Log.d(TAG + "HTTP", sb.toString());
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            bufferedReader.close();
            Log.d(TAG, sb.toString());
            return new JSONObject(sb.toString());
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static JSONObject sendHttpsRequest(JSONObject dataChunk) {
        try {
            URL url = new URL(dataChunk.getJSONObject("data").getString("url"));
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            OutputStream outputStream = connection.getOutputStream();
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            bufferedOutputStream.write(dataChunk.toString().getBytes());
            bufferedOutputStream.flush();
            bufferedOutputStream.close();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            StringBuilder sb = new StringBuilder();
            Log.d(TAG + "HTTPS", sb.toString());
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            bufferedReader.close();
            Log.d(TAG, sb.toString());
            return new JSONObject(sb.toString());
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
