package com.mobiotics.videoapplication.Signin;

import android.app.Activity;

import com.mobiotics.videoapplication.BasePresenter;
import com.mobiotics.videoapplication.BaseView;
import com.mobiotics.videoapplication.Util.NetworkStatus;
import com.mobiotics.videoapplication.modal.pojo.Video;

import java.util.List;

public interface GoogleSignInContract {

    interface View extends BaseView<Presenter> {

        /**
         * show error on snackbar
         * @param errorMsg msg to display
         */
        void showError(String errorMsg);

        /**
         * gets the error string
         * @param resourceId requested string
         * @return
         */
        String getErrorString(int resourceId);

        /**
         * setup and init UI element
         */
        void setUpUI();

        /**
         * google signin
         */
        void signIn();

    }

    interface Presenter extends BasePresenter {

        /**
         * gets filtered error
         * @param errorCode error Id
         * @return
         */
        String filterError(Object errorCode);

        /**
         * google signIn
         */
        void googleSignIn();

        /**
         * set error code
         * @param errorCode
         */
        void setErrorCode(int errorCode);

    }
}
