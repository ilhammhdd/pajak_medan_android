package com.pajakmedan.pajakmedan;

import android.content.Intent;
import android.widget.EditText;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.orhanobut.hawk.Hawk;

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
