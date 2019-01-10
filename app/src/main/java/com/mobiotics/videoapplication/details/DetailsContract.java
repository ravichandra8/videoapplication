package com.mobiotics.videoapplication.details;

import com.mobiotics.videoapplication.BasePresenter;
import com.mobiotics.videoapplication.BaseView;
import com.mobiotics.videoapplication.modal.pojo.Response;
import java.util.List;

public interface DetailsContract {

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
         * gets the error string
         * @param resourceId requested string
         * @return
         */
        String getErrorString(int resourceId);

        /**
         * setup and init UI element
         */
        void setUpUI();

        void getParticularVideo(Response response);

        void getRemainingContent(List<Response> responses);

    }

    interface Presenter extends BasePresenter {

        /**
         * gets filtered error
         * @param errorCode error Id
         * @return
         */
        String filterError(Object errorCode);

        /**
         * show full Image
         * @param id image url
         */
        void getParticularContent(String id);

        void getRemainingContent(String id);

    }

    interface ImageClickListener {

        void onImageClick(String position);
    }
}
