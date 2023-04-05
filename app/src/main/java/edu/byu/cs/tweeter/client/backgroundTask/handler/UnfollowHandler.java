package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;

public class UnfollowHandler extends BackgroundTaskHandler{

    public UnfollowHandler(ServiceObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
        ((FollowService.unfollowObserver)observer).unfollowSucceed(data.getBoolean(UnfollowTask.SUCCESS_KEY));
    }
}
