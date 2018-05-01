package com.pajakmedan.pajakmedan.asynctasks;

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

    public static JSONObject sendRequest(String stringUrl, JSONObject data, String contentType, String token) {
        if (URLUtil.isHttpUrl(stringUrl)) {
            return sendHttpRequest(stringUrl, data, contentType, token);
        } else if (URLUtil.isHttpsUrl(stringUrl)) {
            return sendHttpsRequest(stringUrl, data, contentType, token);
        }

        return null;
    }

    private static JSONObject sendHttpRequest(String stringUrl, JSONObject data, String contentType, String token) {
        try {
            URL url = new URL(stringUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Content-Type", contentType);
            httpURLConnection.setRequestProperty("X-PajaMedan-Token", token);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            bufferedOutputStream.write(data.toString().getBytes());
            bufferedOutputStream.flush();
            bufferedOutputStream.close();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            bufferedReader.close();
            return new JSONObject(sb.toString());
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static JSONObject sendHttpsRequest(String stringUrl, JSONObject data, String contentType, String token) {
        try {
            URL url = new URL(stringUrl);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", contentType);
            connection.setRequestProperty("X-PajakMedan-Token", token);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            OutputStream outputStream = connection.getOutputStream();
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            bufferedOutputStream.write(data.toString().getBytes());
            bufferedOutputStream.flush();
            bufferedOutputStream.close();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            bufferedReader.close();
            return new JSONObject(sb.toString());
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
