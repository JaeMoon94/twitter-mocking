package edu.byu.cs.tweeter.client.model.service;

import androidx.annotation.NonNull;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.client.R;
import edu.byu.cs.tweeter.client.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.backgroundTask.handler.BackgroundTaskHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.FollowHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.GetFollowersCountHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.GetFollowingCountHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.IsFollowerHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.UnfollowHandler;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {

    /**
    FollowerCount
     */

    public interface followerCountObserver extends ServiceObserver
    {
        void setFollowerCount(int count);
//        void followerCountFailed(String message);
//        void followerCountException(Exception e);
    }

    public void setFollwerCount(User user, followerCountObserver observer)
    {

        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(Cache.getInstance().getCurrUserAuthToken(),
                user, new GetFollowersCountHandler(observer));
        BackgroundTaskUtils.runTask(followersCountTask);
    }

    /**
    FollowingCount
     */

    public interface followingCountObserver extends ServiceObserver
    {
        void setFollowingCount(int count);
//        void followingCountFailed(String message);
//        void followingCountException(Exception e);
    }

    public void setFollowingCount(User user, followingCountObserver observer)
    {
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(),
                user, new GetFollowingCountHandler(observer));
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(followingCountTask);
    }



    /**
     * Update Following and Follower count
     */

    public void updateSelectedUserFollowingAndFollowers(User user, followerCountObserver followerCountObserver,
                                                        followingCountObserver followingCountObserver) {
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(Cache.getInstance().getCurrUserAuthToken(),
                user, new GetFollowersCountHandler(followerCountObserver));
        BackgroundTaskUtils.runTask(followersCountTask);

        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(),
                user, new GetFollowingCountHandler(followingCountObserver));
        BackgroundTaskUtils.runTask(followingCountTask);
    }


    /**
    Follow
     */

    public interface FollowObserver extends ServiceObserver
    {
        void followSucceed(boolean val);
//        void displayFollowFailedMessage(String message);
//        void displayFollowErrorMessage(Exception e);

    }

    public void follow(User user, FollowObserver observer)
    {
        FollowTask followTask = new FollowTask(Cache.getInstance().getCurrUserAuthToken(),
                user, new FollowHandler(observer));
        BackgroundTaskUtils.runTask(followTask);
    }


    /**
    Unfollow
     */

    public interface unfollowObserver extends ServiceObserver
    {
        void unfollowSucceed(boolean val);
//        void displayUnFollowFailedMessage(String message);
//        void displayUnFollowExceptionMessage(Exception e);
    }

    public void unFollow(User user, unfollowObserver observer)
    {
        UnfollowTask unfollowTask = new UnfollowTask(Cache.getInstance().getCurrUserAuthToken(),
                user, new UnfollowHandler(observer));
        BackgroundTaskUtils.runTask(unfollowTask);

    }


    /**
     * Is Follower
     */

    public interface IsFollowerObserver extends ServiceObserver{
        void setIsfollowerButton();
        void setIsnotFollowerButton();
//        void isFollowerFailed(String message);
//        void isFollowerException(Exception e);
    }

    public void isFollower(User user, IsFollowerObserver observer)
    {
        IsFollowerTask isFollowerTask = new IsFollowerTask(Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser(), user, new IsFollowerHandler(observer));
        BackgroundTaskUtils.runTask(isFollowerTask);
    }


}
