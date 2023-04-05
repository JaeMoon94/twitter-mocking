package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FeedService;
import edu.byu.cs.tweeter.client.model.service.StoryService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresnter extends ItemPresenter<Status> {

//    private View view;
    public StoryPresnter(ItemView view){
        this.view = view;
    }

    @Override
    public void getService(User user) {
        new StoryService().getStoryService(user, PAGE_SIZE, lastItem, this);
    }
}
