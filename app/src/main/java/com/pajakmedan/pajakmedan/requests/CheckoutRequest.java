package com.pajakmedan.pajakmedan.requests;

import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.asynctasks.MyAsyncTask;
import com.pajakmedan.pajakmedan.listeners.AsyncTaskListener;
import com.pajakmedan.pajakmedan.models.BasketGoods;
import com.pajakmedan.pajakmedan.models.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CheckoutRequest extends BaseRequest {
    public void getCheckoutGoods(JSONObject requestData) {
        final MyAsyncTask myAsyncTask = new MyAsyncTask(Constants.DOMAIN + "api/get-checkout-basket-goods", Constants.getUserToken());
        myAsyncTask.setListener(new AsyncTaskListener() {
            @Override
            public void onPreExecute() {
                executeAsyncTaskListener.onPreExecute(myAsyncTask);
            }

            @Override
            public Object doInBackground(JSONObject... jsonObjects) {
                JSONObject response = RequestPost.sendRequest(myAsyncTask.url, jsonObjects[0], Constants.CONTENT_TYPE, myAsyncTask.token);
                assert response != null;
                try {
                    return BasketGoods.getCheckoutBasketGoods(response.getJSONObject(Constants.RESPONSE_DATA_KEY).getJSONArray("checkout_basket_goods"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void onPostExecute(Object t) {
                executeAsyncTaskListener.onPostExecute(t);
            }
        });
        myAsyncTask.execute(requestData);
    }

    public void getIssuedCheckout() {
        final MyAsyncTask myAsyncTask = new MyAsyncTask(Constants.DOMAIN + "api/get-issued-checkout", Constants.getUserToken());
        myAsyncTask.setListener(new AsyncTaskListener() {
            @Override
            public void onPreExecute() {
                executeAsyncTaskListener.onPreExecute(myAsyncTask);
            }

            @Override
            public Object doInBackground(JSONObject... jsonObjects) {
                JSONObject response = RequestGet.sendRequest(myAsyncTask.url, Constants.CONTENT_TYPE, myAsyncTask.token);
                List<Order> orderList = new ArrayList<>();
                try {
                    if (response != null) {
                        JSONArray jsonArray = response.getJSONObject(Constants.RESPONSE_DATA_KEY).getJSONArray("orders");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            orderList.add(
                                    Order.getAndSaveOrderFromJSON(
                                            jsonArray.getJSONObject(i)
                                    ));
                        }
                        return orderList;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void onPostExecute(Object t) {
                executeAsyncTaskListener.onPostExecute(t);
            }
        });
        myAsyncTask.execute();
    }

    public void issueCheckout(JSONObject requestData){
        final MyAsyncTask myAsyncTask = new MyAsyncTask(Constants.DOMAIN + "api/post-issue-checkout", Constants.getUserToken());
        myAsyncTask.setListener(new AsyncTaskListener() {
            @Override
            public void onPreExecute() {
                executeAsyncTaskListener.onPreExecute(myAsyncTask);
            }

            @Override
            public Object doInBackground(JSONObject... jsonObjects) {
                return RequestPost.sendRequest(myAsyncTask.url, jsonObjects[0], Constants.CONTENT_TYPE, myAsyncTask.token);
            }

            @Override
            public void onPostExecute(Object t) {
                executeAsyncTaskListener.onPostExecute(t);
            }
        });
        myAsyncTask.execute(requestData);
    }

    public void getPaymentExpired(JSONObject requestData){
        final MyAsyncTask myAsyncTask = new MyAsyncTask(Constants.DOMAIN + "api/get-payment-expired", Constants.getUserToken());
        myAsyncTask.setListener(new AsyncTaskListener() {
            @Override
            public void onPreExecute() {
                executeAsyncTaskListener.onPreExecute(myAsyncTask);
            }

            @Override
            public Object doInBackground(JSONObject... jsonObjects) {
                return RequestPost.sendRequest(myAsyncTask.url, jsonObjects[0], Constants.CONTENT_TYPE, myAsyncTask.token);
            }

            @Override
            public void onPostExecute(Object t) {
                executeAsyncTaskListener.onPostExecute(t);
            }
        });
        myAsyncTask.execute(requestData);
    }
}
