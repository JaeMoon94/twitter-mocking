package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowerService;
import edu.byu.cs.tweeter.client.model.service.FollowingService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends ItemPresenter<User>{


    public FollowingPresenter(ItemView view)
    {
        this.view = view;
    }



    @Override
    public void getService(User user){
        new FollowingService().getFollowingService(user, PAGE_SIZE, lastItem, this);
    }

}
