package com.mobiotics.videoapplication.Signin;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.mobiotics.videoapplication.R;
import com.mobiotics.videoapplication.Util.ErrorCode;
import com.mobiotics.videoapplication.Util.NetworkStatus;
import com.mobiotics.videoapplication.modal.VideoDataSource.LoadCallBackListener;
import com.mobiotics.videoapplication.modal.VideoRepository;
import com.mobiotics.videoapplication.modal.pojo.Video;

import java.util.List;

public class GoogleSignInPresenter implements GoogleSignInContract.Presenter {

    private GoogleSignInContract.View view;

    public GoogleSignInPresenter(GoogleSignInContract.View view) {
        this.view = view;
        this.view.setPresenter(this);

    }

    @Override
    public String filterError(Object errorCode) {
        String errorMsg;
        switch ((int) errorCode) {
            case ErrorCode.CONNECTION_PROBLEM:
                errorMsg = view.getErrorString(R.string.connection_problem);
                break;
            case ErrorCode.NETWORK_ERROR:
                errorMsg = view.getErrorString(R.string.network_problem);
                break;

            case ErrorCode.NO_RESULT:
                errorMsg = view.getErrorString(R.string.no_result);
                break;
            default:
                errorMsg = view.getErrorString(R.string.connection_problem);
                break;
        }
        return errorMsg;
    }

    @Override
    public void googleSignIn() {
        view.signIn();
    }

    @Override
    public void setErrorCode(int errorCode) {
        view.showError(filterError(errorCode));
    }

    @Override
    public void start() {
        view.setUpUI();

    }
}
