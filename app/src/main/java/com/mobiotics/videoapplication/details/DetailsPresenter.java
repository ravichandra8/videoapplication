package com.mobiotics.videoapplication.details;

import com.mobiotics.videoapplication.Util.ErrorCode;
import com.mobiotics.videoapplication.modal.ImageDataSource.LoadCallBackListener;
import com.mobiotics.videoapplication.modal.ImageRepository;
import com.mobiotics.videoapplication.modal.pojo.Response;
import com.mobiotics.videoapplication.R;
import java.util.List;

public class DetailsPresenter implements DetailsContract.Presenter {

    private DetailsContract.View view;
    private ImageRepository repository;

    public DetailsPresenter(DetailsContract.View view, ImageRepository repository) {
        this.view = view;
        this.repository = repository;
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
    public void getParticularContent(String id) {
        repository.getParticularVideo(id, new LoadCallBackListener() {
            @Override
            public void onLoaded(Object response) {
                view.getParticularVideo((Response) response);
            }

            @Override
            public void onError(Object error) {
            //ToDO: need to handle error message
            }
        });
    }

    @Override
    public void getRemainingContent(String id) {
        repository.getRemainingContent(id, new LoadCallBackListener() {
            @Override
            public void onLoaded(Object response) {
                view.getRemainingContent((List<Response>) response);
            }

            @Override
            public void onError(Object error) {

            }
        });
    }


    @Override
    public void start() {
        view.setUpUI();
    }
}
