package com.pajakmedan.pajakmedan.models;

import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by milha on 3/1/2018.
 */

public class Customer {
    public int customerId;
    public int userId;
    public int profileId;

    public Customer(int customerId, int userId, int profileId) {
        this.customerId = customerId;
        this.userId = userId;
        this.profileId = profileId;
    }

    public static Customer saveCustomer(JSONObject object) {

        try {
            Customer customer = new Customer(
                    object.getInt("id"),
                    object.getInt("user_id"),
                    object.getInt("profile_id")
            );

            Hawk.put(Constants.CUSTOMER_KEY, customer);

            return customer;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
