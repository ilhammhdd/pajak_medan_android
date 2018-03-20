package com.pajakmedan.pajakmedan;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.asynctasks.Login;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseAuthenticationActivity {
    @BindView(R.id.editText_username)
    EditText editText_username;
    @BindView(R.id.editText_password)
    EditText editText_password;

    @Override
    int getContentId() {
        return R.layout.activity_login;
    }

    @Override
    void insideOnCreate() {
        super.insideOnCreate();
        Constants.GOOGLE_API_CLIENT.connect();
    }

    @Override
    int getViewComponentId() {
        return R.id.textView_register_gunakanAkun;
    }

    @OnClick(R.id.button_login)
    void button_login() {
        if (fieldsNotFilled()) {
            textViewResponseStatus.setText(R.string.field_tidak_boleh_kosong);
        } else {
            Hawk.put(Constants.AUTH_TYPE_KEY, 2);
            nativeLogin(editText_username.getText().toString(), editText_password.getText().toString());
        }
    }

    @OnClick(R.id.textView_registerHere)
    void textView_registerHere() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
    }

    @OnClick(R.id.button_login_google)
    void button_login_google() {
        Hawk.put(Constants.AUTH_TYPE_KEY, 1);
        startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(Constants.GOOGLE_API_CLIENT), REQ_CODE);
    }

    @OnClick(R.id.button_login_facebook)
    void button_login_facebook() {
        Hawk.put(Constants.AUTH_TYPE_KEY, 2);
        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends", "email"));
    }

    private boolean fieldsNotFilled() {
        return editText_username.getText().toString().equals("") || editText_password.getText().toString().equals("");
    }
}
