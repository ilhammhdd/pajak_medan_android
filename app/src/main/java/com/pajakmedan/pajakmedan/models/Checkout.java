package com.pajakmedan.pajakmedan.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by milha on 3/7/2018.
 */

public class Checkout {
    public int checkoutId;
    public int paymentId;
    public int basketId;
    public String status;
    public String expired;
    public String issued;

    public Checkout(int checkoutId, int paymentId, int basketId, String status, String expired, String issued) {
        this.checkoutId = checkoutId;
        this.paymentId = paymentId;
        this.basketId = basketId;
        this.status = status;
        this.expired = expired;
        this.issued = issued;
    }

    public static Checkout saveCheckout(JSONObject checkout) {
        try {
            return new Checkout(
                    checkout.getInt("checkout_id"),
                    checkout.getInt("payment_id"),
                    checkout.getInt("basket_id"),
                    checkout.getString("status"),
                    checkout.getString("expired"),
                    checkout.getString("issued")
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
