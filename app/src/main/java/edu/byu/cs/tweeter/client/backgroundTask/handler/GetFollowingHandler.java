package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.client.model.service.observer.GetItemsObserver;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowingHandler extends BackgroundTaskHandler{
    public GetFollowingHandler(GetItemsObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
        ((GetItemsObserver)observer).getItemSucceed((List<User>) data.getSerializable(PagedTask.ITEMS_KEY),
                data.getBoolean(PagedTask.MORE_PAGES_KEY));
    }
}
