package com.pajakmedan.pajakmedan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by milha on 1/24/2018.
 */

public class SearchCustomerActivity extends AppCompatActivity {

    ImageButton imageButton_back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_customer);

        imageButton_back = findViewById(R.id.button_back_search_customer);

        imageButton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
