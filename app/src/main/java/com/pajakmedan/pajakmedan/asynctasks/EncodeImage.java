package com.pajakmedan.pajakmedan.asynctasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;

import com.pajakmedan.pajakmedan.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by milha on 12/29/2017.
 */

public class EncodeImage extends AsyncTask<String, Void, JSONObject> {

    @Override
    protected JSONObject doInBackground(String... strings) {
        Uri fileUri = Uri.parse(strings[0]);

        Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        byte[] array = stream.toByteArray();
        String encodedString = Base64.encodeToString(array, 0);

        JSONObject request = new JSONObject();

        try {
            URL uploadUrl = new URL(Constants.DOMAIN + "api/upload-file");
            request.put("encoded_string", encodedString);

            HttpURLConnection connection = (HttpURLConnection) uploadUrl.openConnection();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
    }
}
