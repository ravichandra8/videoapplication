package com.mobiotics.videoapplication.homescreen;

import com.mobiotics.videoapplication.BasePresenter;
import com.mobiotics.videoapplication.BaseView;
import com.mobiotics.videoapplication.Util.NetworkStatus;
import com.mobiotics.videoapplication.modal.pojo.Video;
import java.util.List;

public interface MainContract {

    interface View extends BaseView<Presenter> {

        /**
         * show/hide loading indicatore
         * @param active status
         */
        void showLoadingIndicator(boolean active);

        /**
         * show error on snackbar
         * @param errorMsg msg to display
         */
        void showError(String errorMsg);

        /**
         * get all the videos
         * @param videoList
         */
        void showListOfVideos(List<Video> videoList);

        /**
         * gets the error string
         * @param resourceId requested string
         * @return
         */
        String getErrorString(int resourceId);

        /**
         * @param position
         */
        void navigateToDetailsPage(String position);

        /**
         * show/hide network error layout
         * @param active status
         */
        void showNetworkError(boolean active);

        /**
         * setup and init UI element
         */
        void setUpUI();

    }

    interface Presenter extends BasePresenter {

        /**
         * get all videos
         * @param networkStatus network interface
         */
        void getListOfVideos(NetworkStatus networkStatus);

        /**
         * gets filtered error
         * @param errorCode error Id
         * @return
         */
        String filterError(Object errorCode);

        /**
         * navigate to detail page
         * @param position
         */
        void navigateToDetailsPage(String position);

    }

    interface ItemClickListener {

        void onItemClick(String position);
    }
}
