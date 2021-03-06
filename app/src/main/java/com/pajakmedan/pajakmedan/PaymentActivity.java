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
        try {
            GetPayment getPayment = new GetPayment();
            getPayment.execute(
                    new JSONObject()
                            .put("data",
                                    new JSONObject()
                                            .put("url", Constants.DOMAIN + "api/get-payment-method")
                                            .put("api_token", Hawk.get(Constants.USER_API_TOKEN_KEY))
                            )
            );
//            List<Payment>
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.imageView_payment_back)
    void backPressed() {
        finish();
    }

    private void postPaymentIssued() {
        Customer customer = Hawk.get(Constants.CUSTOMER_KEY);
        Payment payment = Hawk.get(Constants.CURRENT_PAYMENT_KEY);

        Calendar calendarExpired = Calendar.getInstance();
        Calendar calendarIssued = Calendar.getInstance();
        calendarExpired.add(Calendar.HOUR, 2);
        String expiredDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("en")).format(calendarExpired.getTime());
        String issuedDateTime = new SimpleDateFormat("yyy-MM-dd HH:mm:ss", new Locale("en")).format(calendarIssued.getTime());

        PostIssueCheckout postIssueCheckout = new PostIssueCheckout();
        try {
            postIssueCheckout.execute(new JSONObject()
                    .put("data", new JSONObject()
                            .put("url", Constants.DOMAIN + "api/post-issue-checkout")
                            .put("customer_id", customer.customerId)
                            .put("api_token", Hawk.get(Constants.USER_API_TOKEN_KEY))
                            .put("payment_id", payment.paymentId)
                            .put("status_id", 2)
                            .put("expired", expiredDateTime)
                            .put("issued", issuedDateTime)
                    )
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        getBasket();
    }
}
