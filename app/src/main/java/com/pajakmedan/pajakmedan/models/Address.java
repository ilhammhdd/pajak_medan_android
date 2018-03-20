package com.pajakmedan.pajakmedan.models;

import android.util.Log;

import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by milha on 3/19/2018.
 */

public class Address {
    public int addressId;
    public int customerId;
    public String name;
    public boolean main;

    public Address(int addressId, int customerId, String name, boolean main) {
        this.addressId = addressId;
        this.customerId = customerId;
        this.name = name;
        this.main = main;
    }

    public static Address saveAddress(JSONObject jsonAddress) {
        try {
            Address address = new Address(
                    jsonAddress.getInt("address_id"),
                    jsonAddress.getInt("customer_id"),
                    jsonAddress.getString("name"),
                    jsonAddress.getInt("main") == 1
            );

            Hawk.put(Constants.MAIN_ADDRESS_KEY, address);
            return address;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Address saveEmptyAddress() {
        Address address = new Address(0, 0, "", false);
        Hawk.put(Constants.MAIN_ADDRESS_KEY, address);
        return address;
    }
}
