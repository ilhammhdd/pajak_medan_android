package com.pajakmedan.pajakmedan.requests;

import android.util.Log;

import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.asynctasks.MyAsyncTask;
import com.pajakmedan.pajakmedan.listeners.AsyncTaskListener;
import com.pajakmedan.pajakmedan.models.Address;
import com.pajakmedan.pajakmedan.models.Payment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomerRequest extends BaseRequest {

    public void getAllAddresses() {
        final MyAsyncTask requestMyAsyncTask = new MyAsyncTask(Constants.DOMAIN + "api/get-all-addresses", Constants.getUserToken());
        requestMyAsyncTask.setListener(new AsyncTaskListener() {
            @Override
            public Object doInBackground(JSONObject... jsonObjects) {
                JSONObject response = RequestGet.sendRequest(requestMyAsyncTask.url, Constants.CONTENT_TYPE, requestMyAsyncTask.token);
                assert response != null;
                try {
                    if (response.getBoolean("success")) {
                        JSONArray addressesJsonArray = response.getJSONObject(Constants.RESPONSE_DATA_KEY).getJSONArray("all_addresses");
                        for (int i = 0; i < addressesJsonArray.length(); i++) {
                            Address.saveAddressToList(Address.extractTheAddress(addressesJsonArray.getJSONObject(i)));
                        }
                    }
                    return Hawk.get(Constants.ALL_ADDRESS_KEY);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void onPostExecute(Object o) {
                Hawk.delete(Constants.ALL_ADDRESS_KEY);
                if (o != null) {
                    executeAsyncTaskListener.onPostExecute(o);
                }
            }
        });
        requestMyAsyncTask.execute();
    }

    public void getAllEvents() {
        final MyAsyncTask myAsyncTask = new MyAsyncTask(Constants.DOMAIN + "api/get-events", Constants.getUserToken());
        myAsyncTask.setListener(new AsyncTaskListener() {
            @Override
            public void onPreExecute() {
                executeAsyncTaskListener.onPreExecute(myAsyncTask);
            }

            @Override
            public Object doInBackground(JSONObject... jsonObjects) {
                Log.d("LOGGING", "SHOW EVENTS => doInBackGround() invoked");
                try {
                    JSONObject response = RequestGet.sendRequest(myAsyncTask.url, Constants.CONTENT_TYPE, myAsyncTask.token);
                    assert response != null;
                    JSONObject responseData = response.getJSONObject(Constants.RESPONSE_DATA_KEY);
                    JSONArray eventsJSON = responseData.getJSONArray("events");

                    HashMap<String, String> events = new HashMap<>();
                    for (int i = 0; i < eventsJSON.length(); i++) {
                        events.put(eventsJSON.getJSONObject(i).getString("name"), eventsJSON.getJSONObject(i).getString("file_path"));
                    }

                    return events;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object t) {
                executeAsyncTaskListener.onPostExecute(t);
            }
        });
        myAsyncTask.execute();
    }

    public void getMainAddress() {
        final MyAsyncTask myAsyncTask = new MyAsyncTask(Constants.DOMAIN + "api/get-main-address", Constants.getUserToken());
        myAsyncTask.setListener(new AsyncTaskListener() {
            @Override
            public void onPreExecute() {
                executeAsyncTaskListener.onPreExecute(myAsyncTask);
            }

            @Override
            public Object doInBackground(JSONObject... jsonObjects) {
                JSONObject response = RequestGet.sendRequest(myAsyncTask.url, Constants.CONTENT_TYPE, myAsyncTask.token);
                assert response != null;
                try {
                    if (response.getBoolean("success")) {
                        return response.getJSONObject(Constants.RESPONSE_DATA_KEY).getJSONObject("main_address");
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

    public void getPaymentMethod() {
        final MyAsyncTask myAsyncTask = new MyAsyncTask(Constants.DOMAIN + "api/get-payment-method", Constants.getUserToken());
        myAsyncTask.setListener(new AsyncTaskListener() {
            @Override
            public void onPreExecute() {
                executeAsyncTaskListener.onPreExecute(myAsyncTask);
            }

            @Override
            public Object doInBackground(JSONObject... jsonObjects) {
                try {
                    List<Payment> paymentList = new ArrayList<>();
                    JSONObject response = RequestGet.sendRequest(myAsyncTask.url, Constants.CONTENT_TYPE, myAsyncTask.token);
                    assert response != null;

                    if (response.getBoolean("success")) {
                        JSONObject responseData = response.getJSONObject(Constants.RESPONSE_DATA_KEY);
                        JSONArray payments = responseData.getJSONArray("payments");
                        for (int i = 0; i < payments.length(); i++) {
                            paymentList.add(
                                    new Payment(
                                            payments.getJSONObject(i).getInt("payment_id"),
                                            payments.getJSONObject(i).getString("payment_image_url"),
                                            payments.getJSONObject(i).getString("payment_name"),
                                            payments.getJSONObject(i).getString("payment_detail")
                                    ));
                        }
                    }
                    return paymentList;
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

    public void postAddAddress(JSONObject requestData) {
        final MyAsyncTask myAsyncTask = new MyAsyncTask(Constants.DOMAIN + "api/post-add-address", Constants.getUserToken());
        myAsyncTask.setListener(new AsyncTaskListener() {
            @Override
            public void onPreExecute() {
                executeAsyncTaskListener.onPreExecute(myAsyncTask);
            }

            @Override
            public Object doInBackground(JSONObject... jsonObjects) {
                JSONObject responseAll = RequestPost.sendRequest(myAsyncTask.url, jsonObjects[0], Constants.CONTENT_TYPE, myAsyncTask.token);
                assert responseAll != null;
                try {
                    return responseAll.getBoolean("success");
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

    public void postDeleteAddress(JSONObject requestData) {
        final MyAsyncTask myAsyncTask = new MyAsyncTask(Constants.DOMAIN + "api/post-delete-address", Constants.getUserToken());
        myAsyncTask.setListener(new AsyncTaskListener() {
            @Override
            public void onPreExecute() {
                executeAsyncTaskListener.onPreExecute(myAsyncTask);
            }

            @Override
            public Object doInBackground(JSONObject... jsonObjects) {
                JSONObject responseAll = RequestPost.sendRequest(myAsyncTask.url, jsonObjects[0], Constants.CONTENT_TYPE, myAsyncTask.token);
                assert responseAll != null;
                try {
                    return responseAll.getBoolean("success");
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

    public void postEditAddress(JSONObject requestData) {
        final MyAsyncTask myAsyncTask = new MyAsyncTask(Constants.DOMAIN + "api/post-edit-address", Constants.getUserToken());
        myAsyncTask.setListener(new AsyncTaskListener() {
            @Override
            public void onPreExecute() {
                executeAsyncTaskListener.onPreExecute(myAsyncTask);
            }

            @Override
            public Object doInBackground(JSONObject... jsonObjects) {
                JSONObject response = RequestPost.sendRequest(myAsyncTask.url, jsonObjects[0], Constants.CONTENT_TYPE, myAsyncTask.token);
                assert response != null;
                return response;
            }

            @Override
            public void onPostExecute(Object t) {
                executeAsyncTaskListener.onPostExecute(t);
            }
        });
        myAsyncTask.execute(requestData);
    }

    public void postEditProfile(JSONObject requestData) {
        final MyAsyncTask myAsyncTask = new MyAsyncTask(Constants.DOMAIN + "api/post-edit-profile", Constants.getUserToken());
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
