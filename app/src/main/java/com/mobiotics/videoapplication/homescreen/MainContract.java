package com.mobiotics.videoapplication.homescreen;

import com.mobiotics.videoapplication.BasePresenter;
import com.mobiotics.videoapplication.BaseView;
import com.mobiotics.videoapplication.Util.NetworkStatus;
import com.mobiotics.videoapplication.modal.pojo.Response;
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
         * display image
         * @param imageList image data
         */
        void showImages(List<Response> imageList);

        /**
         * gets the error string
         * @param resourceId requested string
         * @return
         */
        String getErrorString(int resourceId);

        /**
         * start full image activity
         * @param position image url
         */
        void startFullImageActivity(String position);

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
         * gets the image list
         * @param networkStatus network interface
         */
        void getImages(NetworkStatus networkStatus);

        /**
         * gets filtered error
         * @param errorCode error Id
         * @return
         */
        String filterError(Object errorCode);

        /**
         * show full Image
         * @param position image url
         */
        void showFullImage(String position);

    }

    interface ImageClickListener {

        void onImageClick(String position);
    }
}
