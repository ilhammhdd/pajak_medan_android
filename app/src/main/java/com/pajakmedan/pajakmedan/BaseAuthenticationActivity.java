package com.pajakmedan.pajakmedan;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

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
import com.pajakmedan.pajakmedan.asynctasks.GetBasket;
import com.pajakmedan.pajakmedan.asynctasks.Login;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.models.Basket;
import com.pajakmedan.pajakmedan.models.Customer;
import com.pajakmedan.pajakmedan.models.Profile;
import com.pajakmedan.pajakmedan.models.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by milha on 2/28/2018.
 */

public abstract class BaseAuthenticationActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    GoogleSignInOptions googleSignInOptions;
    CallbackManager callbackManager;
    static int REQ_CODE = 902;

    String TAG = "AUTHENTICATION";

    TextView textViewResponseStatus;

    public static BaseAuthenticationActivity baseAuthenticationActivity;

    @Override
    int getContentId() {
        return 0;
    }

    @Override
    void insideOnCreate() {
        baseAuthenticationActivity = this;
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        Constants.GOOGLE_API_CLIENT = new GoogleApiClient.Builder(getApplicationContext()).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();
        Constants.GOOGLE_API_CLIENT.connect();

        callbackManager = CallbackManager.Factory.create();
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

        textViewResponseStatus = findViewById(getViewComponentId());
    }

    abstract int getViewComponentId();

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    void facebookSignInOnSuccess(LoginResult loginResult) {
        AccessToken accessToken = loginResult.getAccessToken();
        GraphRequest graphRequest = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    Login login = new Login();

                    JSONObject alternativeAuth = new JSONObject();
                    alternativeAuth.put("url", Constants.DOMAIN + "api/alternative-login");
                    alternativeAuth.put("alternative_auth", true);
                    alternativeAuth.put("email", object.getString("email"));
                    alternativeAuth.put("id", object.getString("id"));
                    alternativeAuth.put("full_name", object.getString("name"));
                    alternativeAuth.put("auth_type", "facebook");
                    alternativeAuth.put("photo_url", "https://graph.facebook.com/" + object.getString("id") + "/picture?type=large");

                    JSONObject alternativeAuthChunk = new JSONObject();
                    alternativeAuthChunk.put("data", alternativeAuth);

                    login.execute(alternativeAuthChunk);

                    login.setOnRequestListener(new OnRequestListener() {
                        @Override
                        public void onRequest(JSONObject response) throws JSONException {
                            if (response.getBoolean("authenticated")) {
                                Constants.AUTH_TYPE = 2;
                                User newUser = User.saveCurrentUser(response.getJSONObject("user"), response.has("file_id"));
                                Profile.saveCurrentProfile(response.getJSONObject("profile"));
                                Customer.saveCustomer(response.getJSONObject("customer"));
                                assert newUser != null;
                                Hawk.put(Constants.USER_API_TOKEN_KEY, newUser.apiToken);
                                getBasket();

                                startActivity(new Intent(getApplicationContext(), CustomerHomeActivity.class));
                                finish();
                            } else if (response.getBoolean("email_taken")) {
                                textViewResponseStatus.setText(response.getString("message"));
                                LoginManager.getInstance().logOut();
                                Constants.AUTH_TYPE = -1;
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

    void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();

            Uri photoUrl;
            if (account != null) {
                photoUrl = account.getPhotoUrl();

                Login login = new Login();
                try {
                    JSONObject alternativeAuth = new JSONObject();
                    alternativeAuth.put("url", Constants.DOMAIN + "api/alternative-login");
                    alternativeAuth.put("alternative_auth", true);
                    alternativeAuth.put("email", account.getEmail());
                    alternativeAuth.put("id", account.getId());
                    alternativeAuth.put("full_name", account.getDisplayName());
                    alternativeAuth.put("auth_type", "google");
                    alternativeAuth.put("photo_url", photoUrl == null ? "null" : photoUrl);

                    JSONObject alternativeAuthChunk = new JSONObject();
                    alternativeAuthChunk.put("data", alternativeAuth);

                    login.execute(alternativeAuthChunk);

                    login.setOnRequestListener(new OnRequestListener() {
                        @Override
                        public void onRequest(JSONObject response) throws JSONException {
                            if (response.getBoolean("authenticated")) {
                                Constants.AUTH_TYPE = 1;
                                User newUser = User.saveCurrentUser(response.getJSONObject("user"), response.has("file_id"));
                                Profile.saveCurrentProfile(response.getJSONObject("profile"));
                                Customer.saveCustomer(response.getJSONObject("customer"));
                                assert newUser != null;
                                Hawk.put(Constants.USER_API_TOKEN_KEY, newUser.apiToken);
                                getBasket();

                                startActivity(new Intent(getApplicationContext(), CustomerHomeActivity.class));
                                finish();
                                return;
                            }

                            if (response.getBoolean("email_taken")) {
                                textViewResponseStatus.setText(response.getString("message"));
                                Auth.GoogleSignInApi.signOut(Constants.GOOGLE_API_CLIENT).setResultCallback(new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(@NonNull Status status) {
                                        Constants.AUTH_TYPE = -1;
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

    void nativeLogin(String username, String password) {
        try {
            JSONObject data = new JSONObject();

            data.put("url", Constants.DOMAIN + "/api/login");
            data.put("username", username);
            data.put("password", password);

            JSONObject dataChunk = new JSONObject();
            dataChunk.put("data", data);

            Login login = new Login();

            login.execute(dataChunk);

            login.setOnRequestListener(new OnRequestListener() {
                @Override
                public void onRequest(JSONObject response) throws JSONException {
                    if (response.getBoolean("authenticated")) {
                        Constants.AUTH_TYPE = 0;
                        User newUser = User.saveCurrentUser(response.getJSONObject("user"), response.has("file_id"));
                        Profile.saveCurrentProfile(response.getJSONObject("profile"));
                        Customer.saveCustomer(response.getJSONObject("customer"));
                        assert newUser != null;
                        Hawk.put(Constants.USER_API_TOKEN_KEY, newUser.apiToken);
                        getBasket();

                        startActivity(new Intent(getApplicationContext(), CustomerHomeActivity.class));
                        finish();
                    } else {
                        textViewResponseStatus.setText(response.getString("message"));
                        System.out.println(response.toString());
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getBasket() {
        Customer customer = Hawk.get(Constants.CUSTOMER_KEY);
        try {
            JSONObject request = new JSONObject();
            request.put("url", Constants.DOMAIN + "api/get-basket");
            request.put("api_token", Hawk.get(Constants.USER_API_TOKEN_KEY));
            request.put("customer_id", customer.customerId);

            JSONObject dataChunk = new JSONObject();
            dataChunk.put("data", request);

            GetBasket getBasket = new GetBasket();
            getBasket.execute(dataChunk);

            getBasket.setOnRequestListener(new OnRequestListener() {
                @Override
                public void onRequest(JSONObject jsonObject) throws JSONException {
                    if (jsonObject.has("basket")) {
                        JSONObject basket = jsonObject.getJSONArray("basket").getJSONObject(0);
                        Basket.saveBasket(basket, basket.getString("description") != null);
                        return;
                    }
                    Basket.saveEmptyBasket();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
