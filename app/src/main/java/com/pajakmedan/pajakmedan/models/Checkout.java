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

    public Checkout(int checkoutId, int paymentId, int basketId, String status, String expired) {
        this.checkoutId = checkoutId;
        this.paymentId = paymentId;
        this.basketId = basketId;
        this.status = status;
        this.expired = expired;
    }

    public static Checkout saveCheckout(JSONObject checkout) {
        try {
            return new Checkout(
                    checkout.getInt("checkout_id"),
                    checkout.getInt("payment_id"),
                    checkout.getInt("basket_id"),
                    checkout.getString("status"),
                    checkout.getString("expired")
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
