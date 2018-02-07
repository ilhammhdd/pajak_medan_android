package com.pajakmedan.pajakmedan;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.pajakmedan.pajakmedan.asynctasks.Login;
import com.pajakmedan.pajakmedan.asynctasks.Register;
import com.pajakmedan.pajakmedan.listeners.OnPostListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Created by milha on 1/6/2018.
 */

public class RegisterActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
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
    @BindView(R.id.textView_register_text)
    TextView textView_status;

    GoogleSignInOptions gso;
    GoogleApiClient gac;
    CallbackManager callbackManager;

    private static final int REQ_CODE = 9002;
    private final String TAG = "REGISTER_ACTIVITY";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        if (!Constants.AUTH_TYPE.equals("")) {
            Log.d(TAG, "User already authenticated");
            startActivity(new Intent(getApplicationContext(), CustomerHome.class));
            finish();
        }

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        Constants.GOOGLE_API_CLIENT = new GoogleApiClient.Builder(getApplicationContext()).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        callbackManager = CallbackManager.Factory.create();
        Constants.GOOGLE_API_CLIENT.connect();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        facebookSignInOnSuccess(loginResult);
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException error) {
                    }
                });
    }

    private void facebookSignInOnSuccess(LoginResult loginResult) {
        Log.d(TAG, "Facebook sign in success");
        AccessToken accessToken = loginResult.getAccessToken();
        final Login login = new Login();
        Log.d(TAG, "Sending Facebook graph request");
        GraphRequest graphRequest = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    Log.d(TAG, "Facebook graph request completed");
                    JSONObject alternativeAuth = new JSONObject();
                    alternativeAuth.put("url", Constants.DOMAIN + "api/alternative-login");
                    alternativeAuth.put("alternative_auth", true);
                    alternativeAuth.put("email", object.getString("email"));
                    alternativeAuth.put("id", object.getString("id"));
                    alternativeAuth.put("auth_type", "facebook");
                    alternativeAuth.put("photo_url", "https://graph.facebook.com/" + object.getString("id") + "/picture?type=large");

                    JSONObject alternativeAuthChunk = new JSONObject();
                    alternativeAuthChunk.put("data", alternativeAuth);

                    Log.d(TAG, alternativeAuthChunk.toString());
                    login.execute(alternativeAuthChunk);

                    Log.d(TAG, "Login async task executed");

                    login.setOnPostListener(new OnPostListener() {
                        @Override
                        public void onPost(JSONObject response) throws JSONException {
                            Log.d(TAG, "The response : " + response.toString());
                            if (response.getBoolean("authenticated")) {
                                Log.d(TAG, "User authenticated with facebook authentication");
                                Constants.AUTH_TYPE = "facebook";
                                startActivity(new Intent(getApplicationContext(), CustomerHome.class));
                                finish();
                            }

                            if (response.getBoolean("email_taken")) {
                                textView_status.setText(response.getString("message"));
                                LoginManager.getInstance().logOut();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            Log.d(TAG, "Google authentication success");

            String displayName, email, id, idToken, serverAuthCode;
            Uri photoUrl;
            if (account != null) {
                displayName = account.getDisplayName();
                email = account.getEmail();
                id = account.getId();
                idToken = account.getIdToken();
                serverAuthCode = account.getServerAuthCode();
                photoUrl = account.getPhotoUrl();

                Log.d(TAG, "Google account existed");

                Login login = new Login();
                try {
                    JSONObject alternativeAuth = new JSONObject();
                    alternativeAuth.put("url", Constants.DOMAIN + "api/alternative-login");
                    alternativeAuth.put("alternative_auth", true);
                    alternativeAuth.put("email", email);
                    alternativeAuth.put("id", id);
                    alternativeAuth.put("auth_type", "google");
                    alternativeAuth.put("photo_url", photoUrl == null ? "null" : photoUrl);

                    JSONObject alternativeAuthChunk = new JSONObject();
                    alternativeAuthChunk.put("data", alternativeAuth);

                    Log.d(TAG, "The request : " + alternativeAuthChunk.toString());
                    login.execute(alternativeAuthChunk);
                    Log.d(TAG, "Login async task executed");

                    login.setOnPostListener(new OnPostListener() {
                        @Override
                        public void onPost(JSONObject response) throws JSONException {
                            Log.d(TAG, response.toString());
                            if (response.getBoolean("authenticated")) {
                                Constants.AUTH_TYPE = "google";
                                Log.d(TAG, "User authenticated with google authentication");
                                startActivity(new Intent(getApplicationContext(), CustomerHome.class));
                                finish();
                            }

                            if (response.getBoolean("email_taken")) {
                                textView_status.setText(response.getString("message"));
                                Auth.GoogleSignInApi.signOut(Constants.GOOGLE_API_CLIENT).setResultCallback(new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(@NonNull Status status) {
                                        Log.d(TAG, "Google sign out success");
                                    }
                                });
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @OnClick(R.id.button_register)
    void button_register() {
        Register register = new Register();

        if (!editText_password.getText().toString().equals(editText_confirmPassword.getText().toString())) {
            Log.d(TAG, "Confirmation password doesn't match");
            textView_status.setText(R.string.konfirmasi_password);
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

                register.setOnPostListener(new OnPostListener() {
                    @Override
                    public void onPost(JSONObject response) throws JSONException {
                        Log.d(TAG, "Register response received");
                        textView_status.setText(response.getString("message"));
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.textView_register_loginDisini)
    void textView_register_loginDisini() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    @OnClick(R.id.button_register_google)
    void button_register_google() {
        startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(Constants.GOOGLE_API_CLIENT), REQ_CODE);
    }

    @OnClick(R.id.button_register_facebook)
    void button_register_facebook() {
        LoginManager.getInstance().logInWithReadPermissions(RegisterActivity.this, Arrays.asList("public_profile", "user_friends"));

    }
}
