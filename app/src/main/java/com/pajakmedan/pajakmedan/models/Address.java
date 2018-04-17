package com.pajakmedan.pajakmedan.models;

import android.util.Log;

import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milha on 3/19/2018.
 */

public class Address {
    public int addressId;
    public int customerId;
    public String name;
    public boolean main;

    static List<Address> addressList;

    public Address(int addressId, int customerId, String name, boolean main) {
        this.addressId = addressId;
        this.customerId = customerId;
        this.name = name;
        this.main = main;
    }

    public static Address saveMainAddress(JSONObject jsonAddress) {
        Log.d("LOGGING_MAIN_ADDRESS", "SAVE_TO_HAWK : " + String.valueOf(jsonAddress));
        try {
            Address address = new Address(
                    jsonAddress.getInt("id"),
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

    public static Address extractTheAddress(JSONObject jsonAddress) {
        try {
            return new Address(
                    jsonAddress.getInt("id"),
                    jsonAddress.getInt("customer_id"),
                    jsonAddress.getString("name"),
                    jsonAddress.getInt("main") == 1
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Address saveEmptyMainAddress() {
        Address address = new Address(0, 0, "", false);
        Hawk.put(Constants.MAIN_ADDRESS_KEY, address);
        return address;
    }

    public static void saveAddressToList(Address address) {
        if (Hawk.get(Constants.ALL_ADDRESS_KEY) != null) {
            addressList = Hawk.get(Constants.ALL_ADDRESS_KEY);
            addressList.add(address);
            Hawk.put(Constants.ALL_ADDRESS_KEY, addressList);
            return;
        }

        addressList = new ArrayList<>();
        addressList.add(address);
        Hawk.put(Constants.ALL_ADDRESS_KEY, addressList);
    }

    public static void saveAddressToList(JSONObject jsonAddress) {
        try {
            Address address = new Address(
                    jsonAddress.getInt("id"),
                    jsonAddress.getInt("customer_id"),
                    jsonAddress.getString("name"),
                    jsonAddress.getInt("main") == 1
            );

            if (Hawk.contains(Constants.ALL_ADDRESS_KEY)) {
                addressList.add(address);
                Hawk.put(Constants.ALL_ADDRESS_KEY, addressList);
                return;
            }

            addressList = new ArrayList<>();
            addressList.add(address);
            Hawk.put(Constants.ALL_ADDRESS_KEY, addressList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteAddress(Address address) {
        addressList = Hawk.get(Constants.ALL_ADDRESS_KEY);
        return addressList.remove(address);
    }
}
