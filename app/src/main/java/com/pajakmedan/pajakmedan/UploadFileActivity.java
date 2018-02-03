package com.pajakmedan.pajakmedan;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.File;

/**
 * Created by milha on 12/27/2017.
 */

public class UploadFileActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final Button btn_chooseFromGallery = findViewById(R.id.button_chooseFromGallery);
        final Button btn_uploadFile = findViewById(R.id.button_uploadFile);
    }
}
