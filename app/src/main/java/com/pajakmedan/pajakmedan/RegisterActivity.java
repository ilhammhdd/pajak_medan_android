package com.pajakmedan.pajakmedan;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.pajakmedan.pajakmedan.asynctasks.Register;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * Created by milha on 1/6/2018.
 */

public class RegisterActivity extends BaseAuthenticationActivity {
    @BindView(R.id.editText_register_nama)
    EditText editText_nama;
    @BindView(R.id.editText_register_email)
    EditText editText_email;
    @BindView(R.id.editText_register_noHp)
    EditText editText_noHp;
    @BindView(R.id.editText_register_username)
    EditText editText_username;
    @BindView(R.id.editText_register_password)
    EditText editText_password;
    @BindView(R.id.editText_register_confirmPassword)
    EditText editText_confirmPassword;
//    @BindView(R.id.textView_register_text)
//    TextView textView_status;

    //    GoogleSignInOptions gso;
//    GoogleApiClient gac;
//    CallbackManager callbackManager;
//
//    private static final int REQ_CODE = 9002;
    private final String TAG = "REGISTER_ACTIVITY";

    @Override
    int getContentId() {
        return R.layout.activity_register;
    }

    @Override
    void insideOnCreate() {
        super.insideOnCreate();
        
        if (Hawk.get(Constants.USER_API_TOKEN_KEY) != null) {
            Log.d(TAG, "User already authenticated");
            startActivity(new Intent(RegisterActivity.this, CustomerHomeActivity.class));
            finish();
        }
    }

    @Override
    int getViewComponentId() {
        return R.id.textView_register_text;
    }

    @OnClick(R.id.button_register)
    void button_register() {
        if (fieldsNotFilled()) {
            Log.d(TAG, "Ada field yang kosong");
            textViewResponseStatus.setText(R.string.field_tidak_boleh_kosong);
        } else {
            Register register = new Register();

            if (!editText_password.getText().toString().equals(editText_confirmPassword.getText().toString())) {
                Log.d(TAG, "Confirmation password doesn't match");
                textViewResponseStatus.setText(R.string.konfirmasi_password);
            } else {
                try {
                    JSONObject data = new JSONObject();
                    data.put("url", Constants.DOMAIN + "api/register");
                    data.put("fullName", editText_nama.getText());
                    data.put("phoneNumber", editText_noHp.getText());
                    data.put("email", editText_email.getText());
                    data.put("username", editText_username.getText());
                    data.put("auth_type", "native");
                    data.put("password", editText_password.getText());

                    JSONObject dataChunk = new JSONObject();
                    dataChunk.put("data", data);

                    register.execute(dataChunk);
                    Log.d(TAG, "Register request : " + dataChunk.toString());

                    register.setOnRequestListener(new OnRequestListener() {
                        @Override
                        public <T> void onRequest(T responseGeneric, String key) throws JSONException {
                            JSONObject response = (JSONObject) responseGeneric;
                            JSONObject responseData = response.getJSONObject("response_data");
                            Log.d("RESPONSE_DATA_REGISTER", responseData.toString());
                            switch (responseData.getInt("message")) {
                                case 1: {
                                    Toasty.success(RegisterActivity.this, getResources().getString(R.string.registrasi_berhasil), Toast.LENGTH_SHORT, true).show();
                                    break;
                                }
                                case -1: {
                                    Toasty.error(RegisterActivity.this, getResources().getString(R.string.registrasi_username_atau_email_terpakai), Toast.LENGTH_SHORT, true).show();
                                    break;
                                }
                                case -2: {
                                    Toasty.error(RegisterActivity.this, getResources().getString(R.string.registrasi_gagal), Toast.LENGTH_SHORT, true).show();
                                    break;
                                }
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @OnClick(R.id.textView_register_loginDisini)
    void textView_register_loginDisini() {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }

    @OnClick(R.id.button_register_google)
    void button_register_google() {
        Hawk.put(Constants.AUTH_TYPE_KEY, 1);
        startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(Constants.GOOGLE_API_CLIENT), REQ_CODE);
    }

    @OnClick(R.id.button_register_facebook)
    void button_register_facebook() {
        Hawk.put(Constants.AUTH_TYPE_KEY, 2);
        LoginManager.getInstance().logInWithReadPermissions(RegisterActivity.this, Arrays.asList("public_profile", "user_friends", "email", "user_photos"));
    }

    @OnClick(R.id.textView_hubungiKami)
    void buttonHubungiKami() {

    }

    private boolean fieldsNotFilled() {
        return editText_nama.getText().toString().equals("") ||
                editText_noHp.getText().toString().equals("") ||
                editText_email.getText().toString().equals("") ||
                editText_username.getText().toString().equals("") ||
                editText_password.getText().toString().equals("");
    }
}
