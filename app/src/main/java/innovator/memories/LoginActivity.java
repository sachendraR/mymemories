package innovator.memories;

import android.content.Intent;
import android.support.annotation.Nullable;

import android.os.Bundle;
import android.view.View;

import com.firebase.client.Firebase;
import com.google.android.gms.common.SignInButton;

import innovator.memories.managers.GoogleAuthManager;

/**
 * A login screen that offers login via Google
 */
public class LoginActivity extends BaseActivity  {

    private static final String TAG = "LoginActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);

        if(signInButton != null) {
            signInButton.setSize(SignInButton.SIZE_STANDARD);

            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GoogleAuthManager.getManager(LoginActivity.this).signIn(LoginActivity.this);
                }
            });
        }

    }

    private void updateUI(boolean b) {
    }

    @Override
    public void onStart() {
        super.onStart();

        GoogleAuthManager.getManager(this).onStartSetup();

    }

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       GoogleAuthManager.getManager(this).onActivityResult(requestCode, resultCode, data);
    }
    // [END onActivityResult]

}

