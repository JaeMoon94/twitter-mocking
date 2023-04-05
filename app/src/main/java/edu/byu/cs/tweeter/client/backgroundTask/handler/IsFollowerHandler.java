package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;

public class IsFollowerHandler extends BackgroundTaskHandler{
    public IsFollowerHandler(ServiceObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
        boolean success = data.getBoolean(IsFollowerTask.SUCCESS_KEY);
        if (success) {
            boolean isFollower = data.getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);

            // If logged in user if a follower of the selected user, display the follow button as "following"
            if (isFollower) {
                ((FollowService.IsFollowerObserver) observer).setIsfollowerButton();
            } else {
                ((FollowService.IsFollowerObserver) observer).setIsnotFollowerButton();
            }
        }
    }
}
