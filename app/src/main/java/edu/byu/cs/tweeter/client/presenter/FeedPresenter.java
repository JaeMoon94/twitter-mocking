package edu.byu.cs.tweeter.client.presenter;

import android.content.Intent;
import android.net.Uri;

import java.util.List;
import edu.byu.cs.tweeter.client.model.service.FeedService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends ItemPresenter<Status> {



//    private View view;
    public FeedPresenter(ItemView view) {
        this.view = view;
    }

    @Override
    public void getService(User user) {
        new FeedService().getFeedService(user,PAGE_SIZE,lastItem,this);
    }

}
