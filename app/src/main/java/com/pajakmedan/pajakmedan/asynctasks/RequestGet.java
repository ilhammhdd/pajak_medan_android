package com.pajakmedan.pajakmedan.asynctasks;

import android.util.Log;
import android.webkit.URLUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by milha on 2/19/2018.
 */

public class RequestGet {

    private static String TAG = "REQUEST_GET: ";

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
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

            String line;
            StringBuilder sb = new StringBuilder();
            Log.d(TAG + "HTTP", sb.toString());
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

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
            connection.setRequestMethod("GET");
            connection.setDoInput(true);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            StringBuilder sb = new StringBuilder();
            Log.d(TAG + "HTTPS", sb.toString());
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            return new JSONObject(sb.toString());
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
