package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.FeedService;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;

public class FollowHandler extends BackgroundTaskHandler{
    public FollowHandler(ServiceObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
        ((FollowService.FollowObserver)observer).followSucceed(false);
    }
}
