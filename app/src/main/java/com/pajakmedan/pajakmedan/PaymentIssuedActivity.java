package com.pajakmedan.pajakmedan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.models.Payment;
import com.pajakmedan.pajakmedan.service.BroadcastService;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by milha on 3/4/2018.
 */

public class PaymentIssuedActivity extends BaseActivity {
    @BindView(R.id.imageView_paymentIssued_image)
    ImageView imageViewImage;
    @BindView(R.id.textView_paymentIssued_rekening)
    TextView textViewRekening;
    @BindView(R.id.textView_paymentIssued_countdown)
    TextView textViewCountDown;

    @Override
    int getContentId() {
        return R.layout.activity_payment_issued;
    }

    @Override
    void insideOnCreate() {
        Payment payment = Hawk.get(Constants.CURRENT_PAYMENT_KEY);
        Glide.with(PaymentIssuedActivity.this).load(payment.paymentImageUrl).into(imageViewImage);
        textViewRekening.setText(String.valueOf(payment.paymentDetail));
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateCountDown(intent);
        }
    };

    void updateCountDown(Intent intent) {
        long millisUntilFinished = intent.getLongExtra("countdown", 0);
        textViewCountDown.setText("" + String.format("%d jam %d menit %d detik",
                TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(BroadcastService.COUNTDOWN_BR));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onStop() {
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, BroadcastService.class));
        super.onDestroy();
    }

    @OnClick(R.id.imageView_paymentIssued_back)
    void backPressed() {
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
