package com.mobiotics.videoapplication.Signin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.mobiotics.videoapplication.R;
import com.mobiotics.videoapplication.Util.AppNetworkStatus;
import com.mobiotics.videoapplication.Util.ErrorCode;
import com.mobiotics.videoapplication.homescreen.MainActivity;

import dmax.dialog.SpotsDialog;

public class GoogleSignIn extends AppCompatActivity implements GoogleSignInContract.View{

    private SignInButton googleButton;
    private static final int RC_SIGN_IN=1;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private GoogleSignInPresenter presenter;
    private AppNetworkStatus networkStatus;
    private RelativeLayout relativeLayout;
    private AlertDialog alertDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.googlesignin);

        presenter=new GoogleSignInPresenter(this);
        presenter.start();

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               presenter.googleSignIn();
            }
        });
    }
    @Override
    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
           GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = result.getSignInAccount();
            if(networkStatus.isOnline()) {
                alertDialog.show();
                firebaseAuthWithGoogle(account);
            }
            else{
                presenter.setErrorCode(ErrorCode.NETWORK_ERROR);
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        alertDialog.dismiss();
                        if (task.isSuccessful()) {
                            Intent i=new Intent(GoogleSignIn.this,MainActivity.class);
                            startActivity(i);
                        } else {
                           presenter.setErrorCode(ErrorCode.CONNECTION_PROBLEM);
                        }
                    }
                });
    }

    @Override
    public void showError(String errorMsg) {
        Snackbar.make(relativeLayout, errorMsg, Snackbar.LENGTH_LONG)
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

    @Override
    public String getErrorString(int resourceId) {
        return getResources().getString(resourceId);
    }

    @Override
    public void setUpUI() {
        Toolbar toolbar = findViewById(R.id.tabanim_toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);
        alertDialog = new SpotsDialog(GoogleSignIn.this);

        relativeLayout=findViewById(R.id.parentlayout);
        googleButton=findViewById(R.id.googlebutton);
        networkStatus=new AppNetworkStatus(this);
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient=new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        presenter.setErrorCode(ErrorCode.CONNECTION_PROBLEM);
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();

    }

    @Override
    public void setPresenter(GoogleSignInContract.Presenter presenter) {
        this.presenter=(GoogleSignInPresenter)presenter;
    }
}
