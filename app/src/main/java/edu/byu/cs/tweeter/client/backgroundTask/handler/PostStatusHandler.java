package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.PostStatusService;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;

public class PostStatusHandler extends BackgroundTaskHandler{

    public PostStatusHandler(ServiceObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
        ((PostStatusService.PostStatusObserver)observer).postStatusSucceed("Successfully Posted!");
    }
}
