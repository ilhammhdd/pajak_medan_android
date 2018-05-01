package com.pajakmedan.pajakmedan.models;

import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by milha on 2/27/2018.
 */

public class User {
    public int id;
    public int roleId;
    public int loginTypeId;
    public int fileId;
    public String email;
    public String username;
    public String apiToken;

    public User(int id, int roleId, int loginTypeId, int fileId, String email, String username, String apiToken) {
        this.id = id;
        this.roleId = roleId;
        this.loginTypeId = loginTypeId;
        this.fileId = fileId;
        this.email = email;
        this.username = username;
        this.apiToken = apiToken;
    }

    public static User saveCurrentUser(JSONObject jsonObject) {
        try {
            User user = new User(
                    jsonObject.getInt("id"),
                    jsonObject.getInt("role_id"),
                    jsonObject.getInt("login_type_id"),
                    jsonObject.has("file_id") ? jsonObject.getInt("file_id") : 0,
                    jsonObject.getString("email"),
                    jsonObject.getString("username"),
                    jsonObject.getString("token")
            );
            Hawk.put(Constants.USER_KEY, user);

            return user;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
