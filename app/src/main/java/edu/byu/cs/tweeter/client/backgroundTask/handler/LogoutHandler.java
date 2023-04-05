package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.LogoutService;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;

public class LogoutHandler extends BackgroundTaskHandler{

    public LogoutHandler(ServiceObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
        ((LogoutService.LogoutObserver)observer).logoutSucceed();
    }
}
