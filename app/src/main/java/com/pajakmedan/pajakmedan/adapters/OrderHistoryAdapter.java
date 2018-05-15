package com.pajakmedan.pajakmedan.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.OrderGoodsActivity;
import com.pajakmedan.pajakmedan.PaymentIssuedActivity;
import com.pajakmedan.pajakmedan.R;
import com.pajakmedan.pajakmedan.asynctasks.PostRetrievePaymentExpired;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.models.Order;
import com.pajakmedan.pajakmedan.service.BroadcastService;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

/**
 * Created by milha on 4/2/2018.
 */

public class OrderHistoryAdapter extends BaseAdapter {

    private List<Order> orderList;

    public OrderHistoryAdapter(Context context, List<Order> checkoutList) {
        super(context);
        this.orderList = checkoutList;
    }

    @Override
    public OrderHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = super.layoutInflater.inflate(R.layout.order_history, parent, false);
        return new OrderHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        OrderHistoryViewHolder viewHolder = (OrderHistoryViewHolder) holder;
        viewHolder.textViewTotalPrice.setText(String.valueOf(orderList.get(position).totalPrice));
        viewHolder.textViewTotalItem.setText(String.valueOf(orderList.get(position).totalItems));
        viewHolder.textViewIssued.setText(orderList.get(position).issued);
        if (orderList.get(position).status.equals("payment_approved")) {
            viewHolder.textViewStatus.setText(context.getResources().getString(R.string.berhasil));
            viewHolder.textViewStatus.setTextColor(context.getResources().getColor(R.color.colorWhite));
            viewHolder.textViewStatus.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        } else if (orderList.get(position).status.equals("payment_issued")) {
            viewHolder.textViewStatus.setText(context.getResources().getString(R.string.menunggu));
            viewHolder.textViewStatus.setTextColor(context.getResources().getColor(R.color.colorWhite));
            viewHolder.textViewStatus.setBackgroundColor(context.getResources().getColor(R.color.colorDarkGray));
        } else if (orderList.get(position).status.equals("payment_failed") || orderList.get(position).status.equals("payment_expired")) {
            viewHolder.textViewStatus.setText(context.getResources().getString(R.string.kadaluarsa));
            viewHolder.textViewStatus.setTextColor(context.getResources().getColor(R.color.colorWhite));
            viewHolder.textViewStatus.setBackgroundColor(context.getResources().getColor(R.color.colorRedAlert));
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class OrderHistoryViewHolder extends BaseViewHolder {

        TextView textViewTotalPrice;
        TextView textViewTotalItem;
        TextView textViewIssued;
        TextView textViewStatus;
        ImageView imageViewShowMore;
        View view;

        OrderHistoryViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        void initComponent(View itemView) {
            view = itemView;
            textViewTotalPrice = itemView.findViewById(R.id.textView_orderHistoryItem_totalPrice);
            textViewTotalItem = itemView.findViewById(R.id.textView_orderHistoryItem_totalItem);
            textViewIssued = itemView.findViewById(R.id.textView_orderHistoryItem_issued);
            textViewStatus = itemView.findViewById(R.id.textView_orderHistoryItem_status);
            imageViewShowMore = itemView.findViewById(R.id.imageView_orderHistory_showMore);
        }

        @Override
        public void insideContructor() {
            imageViewShowMore.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imageView_orderHistory_showMore: {
                    Log.d("SHOW_MORE", "CLICKED!");
                    DroppyMenuPopup.Builder droppyMenuPopupBuilder = new DroppyMenuPopup.Builder(context, view);
                    droppyMenuPopupBuilder
                            .addMenuItem(new DroppyMenuItem(context.getResources().getString(R.string.lihat_barang)))
                            .addSeparator();
                    droppyMenuPopupBuilder.setOnClick(new DroppyClickCallbackInterface() {
                        @Override
                        public void call(View v, int id) {
                            Hawk.put(Constants.CURRENT_CHECKOUT_ID_KEY, OrderHistoryAdapter.this.orderList.get(getAdapterPosition()).checkoutId);
                            context.startActivity(new Intent(context, OrderGoodsActivity.class));
                        }
                    });

                    DroppyMenuPopup droppyMenuPopup = droppyMenuPopupBuilder.build();
                    droppyMenuPopup.show();
                    break;
                }
                default: {
                    PostRetrievePaymentExpired postRetrievePaymentExpired = new PostRetrievePaymentExpired(Constants.getUserToken());
                    try {
                        postRetrievePaymentExpired.execute(new JSONObject()
                                .put("checkout_id", OrderHistoryAdapter.this.orderList.get(getAdapterPosition()).checkoutId)
                        );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    postRetrievePaymentExpired.setOnRequestListener(new OnRequestListener() {
                        @Override
                        public <T> void onRequest(T responseGeneric, String key) throws JSONException {
                            Log.d("LOGGING", "CHECKOUT_ID => " + String.valueOf(OrderHistoryAdapter.this.orderList.get(getAdapterPosition()).checkoutId));
                            JSONObject responseJson = (JSONObject) responseGeneric;
                            if (responseJson.getBoolean("success")) {
                                JSONObject responseData = responseJson.getJSONObject(Constants.RESPONSE_DATA_KEY);
                                try {
//                                    Date temp = Calendar.getInstance().getTime();
//                                    long longCurrent = temp.getTime();
//                                    long longExpired = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(responseData.getString("expired_time")).getTime();
//                                    long timeLeft = longExpired - longCurrent;
//                                    Hawk.put(Constants.CURRENT_CHECKOUT_EXPIRATION, timeLeft);
                                    long timeLeft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(responseData.getString("expired_time")).getTime() - Calendar.getInstance().getTime().getTime();
                                    BroadcastService.bi.putExtra("time_left",timeLeft);
                                    context.startActivity(new Intent(context, PaymentIssuedActivity.class));
                                    context.startService(new Intent(context, BroadcastService.class));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                return;
                            }
                            Toasty.error(context, context.getResources().getString(R.string.checkout_gagal_expired), Toast.LENGTH_SHORT, true).show();
                        }
                    });
//                    Calendar calendar = Calendar.getInstance();
//                    Calendar currentCalendar = Calendar.getInstance();
//                    String expiration = OrderHistoryAdapter.this.orderList.get(getAdapterPosition()).expired;
//                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
//
//                    long subtractedTime;
//                    try {
//                        calendar.setTime(simpleDateFormat.parse(expiration));
//                        Date expirationDate = calendar.getTime();
//                        Date currentDate = currentCalendar.getTime();
//
//                        long longExpirationDate = expirationDate.getTime();
//                        long longCurrentDate = currentDate.getTime();
//                        subtractedTime = longExpirationDate - longCurrentDate;
//                        Log.d("SUBTRACTED", "EXPIRATION - CURRENT : " + String.valueOf(subtractedTime));
//
//                        Hawk.put(Constants.CURRENT_CHECKOUT_EXPIRATION, subtractedTime);
//                        context.startActivity(new Intent(context, PaymentIssuedActivity.class));
//                        context.startService(new Intent(context, BroadcastService.class));
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        }
    }
}
