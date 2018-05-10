package com.pajakmedan.pajakmedan;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.adapters.PaymentAdapter;
import com.pajakmedan.pajakmedan.asynctasks.GetPayment;
import com.pajakmedan.pajakmedan.asynctasks.PostIssueCheckout;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.models.Basket;
import com.pajakmedan.pajakmedan.models.Customer;
import com.pajakmedan.pajakmedan.models.Payment;
import com.pajakmedan.pajakmedan.service.BroadcastService;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * Created by milha on 3/3/2018.
 */

public class PaymentActivity extends BaseActivity {
    @BindView(R.id.recyclerView_payment)
    RecyclerView recyclerViewPayment;

    @Override
    int getContentId() {
        return R.layout.activity_payment;
    }

    @Override
    void insideOnCreate() {
        showPayment();
    }

    void showPayment() {
        GetPayment getPayment = new GetPayment(String.valueOf(Hawk.get(Constants.USER_API_TOKEN_KEY)));
        getPayment.execute();
        getPayment.setOnRequestListener(new OnRequestListener() {
            @Override
            public <T> void onRequest(T paymentList, String key) {
                PaymentAdapter paymentAdapter = new PaymentAdapter(PaymentActivity.this, (List<Payment>) paymentList);
                paymentAdapter.setClickListener(new PaymentAdapter.ClickListener() {
                    @Override
                    public void clickItem(View view, final Payment payment) {
                        String toastMessage = String.valueOf(
                                payment.paymentId + '\n'
                                        + payment.paymentImageUrl
                                        + '\n' + payment.paymentName
                                        + '\n' + payment.paymentDetail
                        );
                        Toast.makeText(PaymentActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
                        new AlertDialog.Builder(PaymentActivity.this)
                                .setMessage(getResources().getString(R.string.pilih_pembayaran) + " " + payment.paymentName + ", " + getResources().getString(R.string.lanjutkan))
                                .setCancelable(false)
                                .setPositiveButton(R.string.ya, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Hawk.put(Constants.CURRENT_PAYMENT_KEY, payment);
                                        startActivity(new Intent(PaymentActivity.this, PaymentIssuedActivity.class));
                                        startService(new Intent(PaymentActivity.this, BroadcastService.class));

                                        postPaymentIssued();

                                        PaymentActivity.this.finish();
                                        BasketActivity.basketActivity.finish();
                                    }
                                })
                                .setNegativeButton(R.string.tidak, null)
                                .show();
                    }
                });
                recyclerViewPayment.setAdapter(paymentAdapter);
                recyclerViewPayment.setLayoutManager(new LinearLayoutManager(PaymentActivity.this, LinearLayoutManager.VERTICAL, false));
            }
        });
    }

    @OnClick(R.id.imageView_payment_back)
    void backPressed() {
        finish();
    }

    private void postPaymentIssued() {
        Payment payment = Hawk.get(Constants.CURRENT_PAYMENT_KEY);

        Calendar calendarExpired = Calendar.getInstance();
        Calendar calendarIssued = Calendar.getInstance();
        calendarExpired.add(Calendar.HOUR, 2);
        String expiredDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("en")).format(calendarExpired.getTime());
        String issuedDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("en")).format(calendarIssued.getTime());

        PostIssueCheckout postIssueCheckout = new PostIssueCheckout(String.valueOf(Hawk.get(Constants.USER_API_TOKEN_KEY)));
        try {
            postIssueCheckout.execute(new JSONObject()
                    .put("payment_id", payment.paymentId)
                    .put("expired", expiredDateTime)
                    .put("issued", issuedDateTime)
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        postIssueCheckout.setOnRequestListener(new OnRequestListener() {
            @Override
            public <T> void onRequest(T responseGeneric, String key) throws JSONException {
                JSONObject responseJson = (JSONObject) responseGeneric;
                if (responseJson.getBoolean("success")) {
                    Toasty.success(getApplicationContext(), getResources().getString(R.string.berhasil_checkout)).show();
                    return;
                }
                Toasty.error(getApplicationContext(), getResources().getString(R.string.gagal_checkout)).show();
            }
        });

        getBasket();
    }
}
