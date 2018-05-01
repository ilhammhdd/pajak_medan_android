package com.pajakmedan.pajakmedan.asynctasks;

import android.webkit.URLUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by milha on 2/19/2018.
 */

public class RequestGet {

    public static JSONObject sendRequest(String url, String contentType, String token) {

        if (URLUtil.isHttpUrl(url)) {
            return sendHttpRequest(url, contentType, token);
        } else if (URLUtil.isHttpsUrl(url)) {
            return sendHttpsRequest(url, contentType, token);
        }
        return null;
    }

    private static JSONObject sendHttpRequest(String stringUrl, String contentType, String token) {
        try {
            URL url = new URL(stringUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Content-Type", contentType);
            httpURLConnection.setRequestProperty("X-PajakMedan-Token", token);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            return new JSONObject(sb.toString());
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static JSONObject sendHttpsRequest(String stringUrl, String contentType, String token) {
        try {
            URL url = new URL(stringUrl);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", contentType);
            connection.setRequestProperty("X-PajakMedan-Token", token);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            StringBuilder sb = new StringBuilder();
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
