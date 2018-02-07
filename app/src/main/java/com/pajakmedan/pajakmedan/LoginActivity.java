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

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.internal.OpenGraphJSONUtility;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.pajakmedan.pajakmedan.asynctasks.Login;
import com.pajakmedan.pajakmedan.listeners.OnPostListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.w3c.dom.Text;

import java.util.Arrays;

import butterknife.BindView;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    @BindView(R.id.editText_username)
    EditText editText_username;
    @BindView(R.id.editText_password)
    EditText editText_password;
    @BindView(R.id.button_login)
    Button button_login;
    @BindView(R.id.button_login_facebook)
    Button button_facebook_signIn;
    @BindView(R.id.button_login_google)
    Button button_google_signIn;
    @BindView(R.id.textView_registerHere)
    TextView textView_registerHere;
    @BindView(R.id.textView_register_gunakanAkun)
    TextView textView_gunakanAkun;

    GoogleSignInOptions gso;
    GoogleApiClient gac;
    CallbackManager callbackManager;

    private final int REQ_CODE = 9001;
    private final String TAG = "LOGIN_ACTIVITY";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Constants.GOOGLE_API_CLIENT.connect();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        callbackManager = CallbackManager.Factory.create();

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONObject data = new JSONObject();

                    data.put("url", Constants.DOMAIN + "/api/login");
                    data.put("username", editText_username.getText());
                    data.put("password", editText_password.getText());

                    JSONObject dataChunk = new JSONObject();
                    dataChunk.put("data", data);

                    Login login = new Login();

                    login.execute(dataChunk);
                    Log.d(TAG, "Login async task executed");

                    login.setOnPostListener(new OnPostListener() {
                        @Override
                        public void onPost(JSONObject response) throws JSONException {
                            if (response.getBoolean("authenticated")) {
                                Constants.AUTH_TYPE = "native";
                                Log.d(TAG, "User Authenticated with native authentication");

                                startActivity(new Intent(getApplicationContext(), CustomerHome.class));
                                finish();
                            } else {
                                textView_gunakanAkun.setText(response.getString("message"));
                                System.out.println(response.toString());
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        textView_registerHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                finish();
            }
        });

        button_google_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(Constants.GOOGLE_API_CLIENT), REQ_CODE);
            }
        });

        button_facebook_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends", "email"));
            }
        });

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

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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

                    login.execute(alternativeAuthChunk);
                    Log.d(TAG, "The request : " + alternativeAuthChunk.toString());
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
                                textView_gunakanAkun.setText(response.getString("message"));
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

    private void facebookSignInOnSuccess(final LoginResult loginResult) {
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
                            if (response.getBoolean("authenticated")) {
                                Log.d(TAG, "User authenticated with facebook authentication");
                                Constants.AUTH_TYPE = "facebook";
                                startActivity(new Intent(getApplicationContext(), CustomerHome.class));
                                finish();
                            }

                            if (response.getBoolean("email_taken")) {
                                textView_gunakanAkun.setText(response.getString("message"));
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
}
