package com.pajakmedan.pajakmedan.models;

import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by milha on 2/28/2018.
 */

public class Profile {
    public int profileId;
    public String fullName;
    public String phoneNumber;
    public String email;

    public Profile(int profileId, String fullName, String phoneNumber, String email) {
        this.profileId = profileId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public static Profile saveCurrentProfile(JSONObject object) {
        try {
            Profile profile = new Profile(
                    object.getInt("id"),
                    object.getString("full_name"),
                    object.getString("phone_number"),
                    object.getString("email")
            );

            Hawk.put(Constants.PROFILE_KEY, profile);

            return profile;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
