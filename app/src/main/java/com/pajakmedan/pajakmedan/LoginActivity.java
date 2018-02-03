package com.pajakmedan.pajakmedan;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.pajakmedan.pajakmedan.asynctasks.Login;
import com.pajakmedan.pajakmedan.listeners.OnPostListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    Button button_login, button_google_signIn, button_facebook_signIn;
    EditText editText_username, editText_password;
    TextView textView_registerHere, textView_gunakanAkun;

    GoogleSignInOptions gso;
    GoogleApiClient gac;
    CallbackManager callbackManager;

    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;

    private final int REQ_CODE = 9001;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editText_username = findViewById(R.id.editText_username);
        editText_password = findViewById(R.id.editText_password);
        button_login = findViewById(R.id.button_login);
        button_facebook_signIn = findViewById(R.id.button_login_facebook);
        button_google_signIn = findViewById(R.id.button_login_google);
        textView_registerHere = findViewById(R.id.textView_registerHere);
        textView_gunakanAkun = findViewById(R.id.textView_register_gunakanAkun);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gac = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        callbackManager = CallbackManager.Factory.create();

        textView_registerHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

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

                    System.out.println(dataChunk);

                    login.setOnPostListener(new OnPostListener() {
                        @Override
                        public void onPost(JSONObject response) throws JSONException {
                            if (response.getBoolean("authenticated")) {
                                Constants.AUTH_TYPE = "native";

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
                Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        button_google_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(gac);
                startActivityForResult(signInIntent, REQ_CODE);
            }
        });

        button_facebook_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends"));
            }
        });

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        System.out.println("===============Sign in Facebook Success===============");
                        facebookSignInOnSuccess(loginResult);
                    }

                    @Override
                    public void onCancel() {
                        System.out.println("===============Sign in Facebook Canceled===============");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        System.out.println("===============Sign in Facebook Error===============");
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
            System.out.println("==================Sign In Google Success==================");
            GoogleSignInAccount account = result.getSignInAccount();

            String displayName, email, id, idToken, serverAuthCode;
            Uri photoUrl;
            if (account != null) {
                displayName = account.getDisplayName();
                email = account.getEmail();
                id = account.getId();
                idToken = account.getIdToken();
                serverAuthCode = account.getServerAuthCode();
                photoUrl = account.getPhotoUrl();

                System.out.println("==================Display Name : " + displayName + "==================");
                System.out.println("==================Email : " + email + "==================");
                System.out.println("==================Id: " + id + "==================");
                System.out.println("==================Id Token : " + idToken + "==================");
                System.out.println("==================Server Auth Code : " + serverAuthCode + "==================");
                System.out.println("==================Photo Url : " + photoUrl + "==================");

                Constants.AUTH_TYPE = "google";

                finish();

                Login login = new Login();
                try {
                    JSONObject alternativeAuth = new JSONObject();
                    alternativeAuth.put("url", Constants.DOMAIN + "api/alternative-login");
                    alternativeAuth.put("alternative_auth", true);
                    alternativeAuth.put("email", email);
                    alternativeAuth.put("id", id);
                    alternativeAuth.put("photo_url", photoUrl);

                    JSONObject alternativeAuthChunk = new JSONObject();
                    alternativeAuthChunk.put("data", alternativeAuth);

                    login.execute(alternativeAuthChunk);

                    login.setOnPostListener(new OnPostListener() {
                        @Override
                        public void onPost(JSONObject response) throws JSONException {
                            System.out.println("================The response:" + response + "================");
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent customerHomeIntent = new Intent(getApplicationContext(), CustomerHome.class);
                startActivity(customerHomeIntent);

            }
        } else {
            System.out.println("==================Sign in Google Failed==================");
        }
    }

    private void facebookSignInOnSuccess(LoginResult loginResult) {
        AccessToken accessToken = loginResult.getAccessToken();

        if (accessToken != null) {
            System.out.println("==================Token : " + accessToken.getToken() + "==================");
            System.out.println("==================Application Id : " + accessToken.getApplicationId() + "==================");
            System.out.println("==================User Id : " + accessToken.getUserId() + "==================");
            System.out.println("==================Permissions : " + accessToken.getPermissions() + "==================");
        } else {
            accessTokenTracker = new AccessTokenTracker() {
                @Override
                protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                    if (currentAccessToken != null) {
                        System.out.println("==================Token : " + currentAccessToken.getToken() + "==================");
                        System.out.println("==================Application Id : " + currentAccessToken.getApplicationId() + "==================");
                        System.out.println("==================User Id : " + currentAccessToken.getUserId() + "==================");
                        System.out.println("==================Permissions : " + currentAccessToken.getPermissions() + "==================");
                    }
                }
            };

            accessTokenTracker.startTracking();
        }

        Profile profile = Profile.getCurrentProfile();

        if (profile != null) {
            System.out.println("==================First Name : " + profile.getFirstName() + "==================");
            System.out.println("==================Last Name : " + profile.getLastName() + "==================");
            System.out.println("==================Name : " + profile.getName() + "==================");
            System.out.println("==================Id : " + profile.getId() + "==================");
            System.out.println("==================Link URI : " + profile.getLinkUri() + "==================");
            System.out.println("==================Profile Picture URI : " + profile.getProfilePictureUri(300, 300) + "==================");

            Intent customerHomeIntent = new Intent(getApplicationContext(), CustomerHome.class);
            startActivity(customerHomeIntent);
        } else {
            profileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                    if (currentProfile != null) {
                        System.out.println("==================First Name : " + currentProfile.getFirstName() + "==================");
                        System.out.println("==================Last Name : " + currentProfile.getLastName() + "==================");
                        System.out.println("==================Name : " + currentProfile.getName() + "==================");
                        System.out.println("==================Id : " + currentProfile.getId() + "==================");
                        System.out.println("==================Link URI : " + currentProfile.getLinkUri() + "==================");
                        System.out.println("==================Profile Picture URI : " + currentProfile.getProfilePictureUri(300, 300) + "==================");

                        Constants.AUTH_TYPE = "facebook";

                        finish();

                        Intent customerHomeIntent = new Intent(getApplicationContext(), CustomerHome.class);
                        startActivity(customerHomeIntent);
                    }
                }
            };

            profileTracker.startTracking();
        }
    }
}
