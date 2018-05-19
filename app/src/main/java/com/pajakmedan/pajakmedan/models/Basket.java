package com.pajakmedan.pajakmedan.models;

import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by milha on 3/1/2018.
 */

public class Basket {
    public int basketId;
    public int customerId;
    public int total;
    public String description;
    public String status;

    public Basket(int basketId, int customerId, int total, String description, String status) {
        this.basketId = basketId;
        this.customerId = customerId;
        this.total = total;
        this.description = description;
        this.status = status;
    }

    public static void saveEmptyBasket() {
        Hawk.put(Constants.BASKET_KEY, null);
    }

    public static Basket saveBasket(JSONObject object, boolean hasDescription) {
        try {
            Basket basket = new Basket(
                    object.getInt("id"),
                    object.getInt("customer_id"),
                    object.getInt("total"),
                    hasDescription ? object.getString("description") : "",
                    object.getString("status_name")
            );

            Hawk.put(Constants.BASKET_KEY, basket);

            return basket;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
