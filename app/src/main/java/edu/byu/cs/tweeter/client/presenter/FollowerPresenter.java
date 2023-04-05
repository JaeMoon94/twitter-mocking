package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FeedService;
import edu.byu.cs.tweeter.client.model.service.FollowerService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowerPresenter extends ItemPresenter<User>{


//    private View view;
    public FollowerPresenter(ItemView view)
    {
        this.view = view;
    }

    @Override
    public void getService(User user)
    {
        new FollowerService().getFollowerSucceed(user, PAGE_SIZE, lastItem, this);
    }


}
