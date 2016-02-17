package innovator.memories.managers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;

import innovator.memories.LoginActivity;
import innovator.memories.R;
import innovator.memories.models.User;


/**
 * Created by SachendraSingh on 2/11/16.
 */
public class GoogleAuthManager implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private static final int RC_SIGN_IN = 101;
    private static final String TAG = "GoogleAuthManager";
    private static final String SERVER_CLIENT_ID = "341223442621-pta5d5uuoont0snkb46lkh58k5igv0ip.apps.googleusercontent.com";
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;

    private static GoogleAuthManager manager;
    private Activity activity;
    private boolean signOut;
    private ResultCallback callback;

    public GoogleAuthManager(Activity activity) {
        this.activity = activity;

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(SERVER_CLIENT_ID)
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestServerAuthCode(SERVER_CLIENT_ID)
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .enableAutoManage((FragmentActivity) activity /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // [END build_client]

    }

    public static GoogleAuthManager getManager(Activity activity){
        if(manager == null) manager = new GoogleAuthManager(activity);
        manager.activity = activity;
        manager.mGoogleApiClient.registerConnectionCallbacks(manager);
        manager.mGoogleApiClient.connect();
        return manager;
    }

    public void onStartSetup(){
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    // [START handleSignInResult]
    public void handleSignInResult(GoogleSignInResult result) {
        Log.d("GoogleAuthManager", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            final GoogleSignInAccount acct = result.getSignInAccount();

            new AsyncTask<String, String, String>(){

                @Override
                protected String doInBackground(String[] params) {
                    return retrieveToken(acct);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);

                    if (s == null) {
                        Log.e(TAG, "Token returned Null");
                        return;
                    }

                    showProgressDialog(activity.getString(R.string.wait_preparing_data));

                    FireBaseManager.getManager().authWithOAuthToken("google", s, new Firebase.AuthResultHandler() {
                        @Override
                        public void onAuthenticated(AuthData authData) {
                            FlowManager.signInComplete(activity, new User(acct.getDisplayName(), acct.getEmail(), (acct.getPhotoUrl() != null ? acct.getPhotoUrl():"").toString()));
                            hideProgressDialog();
                        }

                        @Override
                        public void onAuthenticationError(FirebaseError firebaseError) {
                            Toast.makeText(activity, firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d(TAG, firebaseError.getDetails());
                            hideProgressDialog();
                        }
                    });

                }

            }.execute((String[])null);

        } else {
            // Signed out, show unauthenticated UI.
        }
        mGoogleApiClient.unregisterConnectionCallbacks(this);
        mGoogleApiClient.disconnect();
    }

    private String retrieveToken(GoogleSignInAccount acct) {
        String scope = String.format("oauth2:%s", Scopes.PLUS_LOGIN);
        try {
            String token = GoogleAuthUtil.getToken(activity, acct.getEmail(), scope);
            return token;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GoogleAuthException e) {
            e.printStackTrace();
        }
        return null;
//        return acct.getServerAuthCode();
    }

    // [END handleSignInResult]

    // [START signIn]
    public void signIn(Activity activity) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

    public void signOut(Activity activity, ResultCallback callback){
//        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
//                callback);
        this.callback = callback;
        signOut = true;
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(activity);
            mProgressDialog.setMessage(activity.getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(activity);
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            GoogleAuthManager.getManager(activity).handleSignInResult(result);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(activity, "Connection Failed, try again ... ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if(signOut)
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                callback);

        signOut = false;
        callback = null;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
