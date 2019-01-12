package com.mobiotics.videoapplication.details;

import com.mobiotics.videoapplication.BasePresenter;
import com.mobiotics.videoapplication.BaseView;
import com.mobiotics.videoapplication.modal.pojo.Video;
import java.util.List;

public interface DetailsContract {

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
         * get the particluar video
         * @param video
         */

        void getParticularVideo(Video video);

        /**
         * get the related videos
         * @param videoList
         */
        void getRelatedVideos(List<Video> videoList);

    }

    interface Presenter extends BasePresenter {

        /**
         * gets filtered error
         * @param errorCode error Id
         * @return
         */
        String filterError(Object errorCode);

        /**
         * get paricular video based on id
         * @param id
         * @param isNext
         */
        void getParticularContent(String id,boolean isNext);

        /**
         * get the
         * @param id
         */
        void getRelatedVideos(String id);

        /**
         * storing the details.
         * @param id
         * @param timestamp
         * @param videoState
         */
        void storevideoPosition(String id,long timestamp,boolean videoState);

        /**
         * when video compeleted delete video details from sqlite.
         * @param id
         */
       void deleteVideoPosition(String id);
    }
}
