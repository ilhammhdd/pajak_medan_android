package com.pajakmedan.pajakmedan;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

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
import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.asynctasks.MyAsyncTask;
import com.pajakmedan.pajakmedan.listeners.ExecuteAsyncTaskListener;
import com.pajakmedan.pajakmedan.models.Customer;
import com.pajakmedan.pajakmedan.models.Profile;
import com.pajakmedan.pajakmedan.models.User;
import com.pajakmedan.pajakmedan.requests.LoginRequest;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

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
                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setListener(new ExecuteAsyncTaskListener() {
                    @Override
                    public void onPreExecute(MyAsyncTask myAsyncTask) {
                        myAsyncTaskList.add(myAsyncTask);
                    }

                    @Override
                    public void onPostExecute(Object t) {
                        JSONObject response = (JSONObject) t;
                        authenticationResponse(response);
                    }
                });
                try {
                    loginRequest.login(false, new JSONObject()
                            .put("alternative_auth", true)
                            .put("email", object.getString("email"))
                            .put("id", object.getString("id"))
                            .put("full_name", object.getString("name"))
                            .put("auth_type", "facebook")
                            .put("photo_url", "https://graph.facebook.com/" + object.getString("id") + "/picture?type=large")
                    );
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

                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setListener(new ExecuteAsyncTaskListener() {
                    @Override
                    public void onPreExecute(MyAsyncTask myAsyncTask) {
                        myAsyncTaskList.add(myAsyncTask);
                    }

                    @Override
                    public void onPostExecute(Object t) {
                        JSONObject response = (JSONObject) t;
                        authenticationResponse(response);
                    }
                });
                try {
                    loginRequest.login(false, new JSONObject()
                            .put("alternative_auth", true)
                            .put("email", account.getEmail())
                            .put("id", account.getId())
                            .put("full_name", account.getDisplayName())
                            .put("auth_type", "google")
                            .put("photo_url", photoUrl == null ? "null" : photoUrl)
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void nativeLogin(String username, String password) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setListener(new ExecuteAsyncTaskListener() {
            @Override
            public void onPreExecute(MyAsyncTask myAsyncTask) {
                myAsyncTaskList.add(myAsyncTask);
            }

            @Override
            public void onPostExecute(Object t) {
                JSONObject response = (JSONObject) t;
                authenticationResponse(response);
            }
        });
        try {
            loginRequest.login(true, new JSONObject()
                    .put("username", username)
                    .put("password", password)
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void authenticationResponse(JSONObject response) {
        try {
            if (response.getBoolean("success")) {
                JSONObject responseData = response.getJSONObject("response_data");

                User newUser = User.saveCurrentUser(responseData.getJSONObject("user"));
                Profile.saveCurrentProfile(responseData.getJSONObject("profile"));
                Customer.saveCustomer(responseData.getJSONObject("customer"));

                assert newUser != null;

                Hawk.put(Constants.USER_API_TOKEN_KEY, newUser.apiToken);
                Hawk.put(Constants.PROFILE_PHOTO, responseData.getString("photo"));

                getBasket();
                getMainAddress();

                startActivity(new Intent(getApplicationContext(), CustomerHomeActivity.class));
                finish();
            } else {
                Toasty.error(getApplicationContext(), getResources().getString(R.string.login_gagal), Toast.LENGTH_SHORT, true).show();
                logout(getApplicationContext());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
