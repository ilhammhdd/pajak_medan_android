package com.pajakmedan.pajakmedan.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by milha on 4/2/2018.
 */

public class Order {
    public int totalPrice;
    public int totalItems;
    public int checkoutId;
    public String issued;
    public String expired;
    public String status;

    public Order(int totalPrice, int totalItems, int checkoutId, String issued, String expired, String status) {
        this.totalPrice = totalPrice;
        this.totalItems = totalItems;
        this.checkoutId = checkoutId;
        this.issued = issued;
        this.expired = expired;
        this.status = status;
    }

    public static Order getAndSaveOrderFromJSON(JSONObject jsonObject) {
        try {
            return new Order(
                    jsonObject.getInt("total_price"),
                    jsonObject.getInt("total_items"),
                    jsonObject.getInt("checkout_id"),
                    jsonObject.getString("issued"),
                    jsonObject.getString("expired"),
                    jsonObject.getString("status")
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
