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
import com.pajakmedan.pajakmedan.asynctasks.Login;
import com.pajakmedan.pajakmedan.asynctasks.Register;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

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
        if (Constants.AUTH_TYPE != -1) {
            Log.d(TAG, "User already authenticated");
            startActivity(new Intent(RegisterActivity.this, CustomerHomeActivity.class));
            finish();
        }
//        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
//        Constants.GOOGLE_API_CLIENT = new GoogleApiClient.Builder(RegisterActivity.this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
//        callbackManager = CallbackManager.Factory.create();
//        Constants.GOOGLE_API_CLIENT.connect();
//
//        LoginManager.getInstance().registerCallback(callbackManager,
//                new FacebookCallback<LoginResult>() {
//                    @Override
//                    public void onSuccess(LoginResult loginResult) {
//                        facebookSignInOnSuccess(loginResult);
//                    }
//
//                    @Override
//                    public void onCancel() {
//                    }
//
//                    @Override
//                    public void onError(FacebookException error) {
//                    }
//                });
    }

    @Override
    int getViewComponentId() {
        return R.id.textView_register_text;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == REQ_CODE) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            handleSignInResult(result);
//        }
//    }

//    private void handleSignInResult(GoogleSignInResult result) {
//        if (result.isSuccess()) {
//            GoogleSignInAccount account = result.getSignInAccount();
//            Log.d(TAG, "Google authentication success");
//
//            String displayName, email, id, idToken, serverAuthCode;
//            Uri photoUrl;
//            if (account != null) {
//                displayName = account.getDisplayName();
//                email = account.getEmail();
//                id = account.getId();
//                idToken = account.getIdToken();
//                serverAuthCode = account.getServerAuthCode();
//                photoUrl = account.getPhotoUrl();
//
//                Log.d(TAG, "Google account existed");
//
//                Login login = new Login();
//                try {
//                    JSONObject alternativeAuth = new JSONObject();
//                    alternativeAuth.put("url", Constants.DOMAIN + "api/alternative-login");
//                    alternativeAuth.put("alternative_auth", true);
//                    alternativeAuth.put("email", email);
//                    alternativeAuth.put("id", id);
//                    alternativeAuth.put("auth_type", "google");
//                    alternativeAuth.put("photo_url", photoUrl == null ? "null" : photoUrl);
//
//                    JSONObject alternativeAuthChunk = new JSONObject();
//                    alternativeAuthChunk.put("data", alternativeAuth);
//
//                    Log.d(TAG, "The request : " + alternativeAuthChunk.toString());
//                    login.execute(alternativeAuthChunk);
//                    Log.d(TAG, "Login async task executed");
//
//                    login.setOnRequestListener(new OnRequestListener() {
//                        @Override
//                        public void onRequest(JSONObject response) throws JSONException {
//                            Log.d(TAG, response.toString());
//                            if (response.getBoolean("authenticated")) {
//                                Constants.AUTH_TYPE = 1;
////                                Constants.USER_API_TOKEN = response.getString("api_token");
////                                User.saveCurrentUser(response.getJSONObject("user"));
//                                Log.d(TAG, "User authenticated with google authentication");
//                                startActivity(new Intent(RegisterActivity.this, CustomerHomeActivity.class));
//                                finish();
//                                return;
//                            }
//
//                            if (response.getBoolean("email_taken")) {
//                                textView_status.setText(response.getString("message"));
//                                Auth.GoogleSignInApi.signOut(Constants.GOOGLE_API_CLIENT).setResultCallback(new ResultCallback<Status>() {
//                                    @Override
//                                    public void onResult(@NonNull Status status) {
//                                        Log.d(TAG, "Google sign out success");
//                                        Constants.AUTH_TYPE = -1;
//                                    }
//                                });
//                            }
//                        }
//                    });
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }

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
                        public void onRequest(JSONObject response) throws JSONException {
                            Log.d(TAG, "Register response received");
                            textViewResponseStatus.setText(response.getString("message"));
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
        startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(Constants.GOOGLE_API_CLIENT), REQ_CODE);
    }

    @OnClick(R.id.button_register_facebook)
    void button_register_facebook() {
        LoginManager.getInstance().logInWithReadPermissions(RegisterActivity.this, Arrays.asList("public_profile", "user_friends", "email", "user_photos"));
    }

    @OnClick(R.id.textView_hubungiKami)
    void buttonHubungiKami() {
        if (Constants.AUTH_TYPE == 2) {
            Log.d(TAG, "User with facebook authentication successfully logout");
            AccessToken.setCurrentAccessToken(null);
            LoginManager.getInstance().logOut();

            Constants.AUTH_TYPE = -1;

            startActivity(new Intent(RegisterActivity.this, RegisterActivity.class));
            finish();
        }
    }

    private boolean fieldsNotFilled() {
        return editText_nama.getText().toString().equals("") ||
                editText_noHp.getText().toString().equals("") ||
                editText_email.getText().toString().equals("") ||
                editText_username.getText().toString().equals("") ||
                editText_password.getText().toString().equals("");
    }
}
