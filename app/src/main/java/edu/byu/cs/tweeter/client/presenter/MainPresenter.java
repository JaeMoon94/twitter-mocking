package edu.byu.cs.tweeter.client.presenter;

import java.text.ParseException;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.FollowerService;
import edu.byu.cs.tweeter.client.model.service.LogoutService;
import edu.byu.cs.tweeter.client.model.service.PostStatusService;
import edu.byu.cs.tweeter.client.model.service.StoryService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter implements LogoutService.LogoutObserver, PostStatusService.PostStatusObserver, FollowService.FollowObserver,
        FollowService.unfollowObserver, FollowService.IsFollowerObserver, FollowService.followingCountObserver, FollowService.followerCountObserver{


    public interface View extends edu.byu.cs.tweeter.client.presenter.View
    {
//        void displayErrorMessage(String message);
//        void displayFailedMessage(String message);
//        void displayInfoMessage(String message);
        void logout();

        void updateFollow(boolean value);
        void updateUnFollow(boolean value);

        void setIsFollowerButton();
        void setIsNotFollowerButton();

        void setFollowingCount(int count);
        void setFollowerCount(int count);
    }

    private View view;

    public MainPresenter(View view)
    {
        this.view = view;
    }

    /**
    Follower and following count
     */
    public void updateSelectedUserFollowingAndFollowers(User user)
    {
        new FollowService().updateSelectedUserFollowingAndFollowers(user, this, this);
    }

    /**
    Follower Count Service
     */

    @Override
    public void setFollowerCount(int count) {
        view.setFollowerCount(count);
    }

//    @Override
//    public void followerCountFailed(String message) {
//        view.displayFailedMessage("Failed to get followers count: " + message);
//    }
//
//    @Override
//    public void followerCountException(Exception e) {
//        view.displayErrorMessage("Failed to get followers count because of exception: " + e.getMessage());
//    }


    /**
    Following Count Service
     */

    @Override
    public void setFollowingCount(int count) {
        view.setFollowingCount(count);
    }

//    @Override
//    public void followingCountFailed(String message) {
//        view.displayFailedMessage("Failed to get following count: " + message);
//    }
//
//    @Override
//    public void followingCountException(Exception e) {
//        view.displayErrorMessage("Failed to get following count because of exception: " + e.getMessage());
//    }

    /**
    IsFollower Service
     */

    public void isFollower(User user)
    {
        new FollowService().isFollower(user, this);
    }

    @Override
    public void setIsfollowerButton() {
        view.setIsFollowerButton();
    }

    @Override
    public void setIsnotFollowerButton() {
        view.setIsNotFollowerButton();
    }

//    @Override
//    public void isFollowerFailed(String message) {
//        view.displayFailedMessage("Failed to determine following relationship: " + message);
//    }
//
//    @Override
//    public void isFollowerException(Exception e) {
//        view.displayErrorMessage("Failed to determine following relationship because of exception: " + e.getMessage());
//    }

    /**
     * UnFollow service
     */

    public void unFollow(User user)
    {
        view.displayInfoMessage("Removing " + user.getName() + "...");
        new FollowService().unFollow(user, this);
    }


    @Override
    public void unfollowSucceed(boolean val) {
        view.updateUnFollow(val);
    }

//    @Override
//    public void displayUnFollowFailedMessage(String message) {
//        view.displayFailedMessage("Failed to unfollow: " + message);
//    }
//
//    @Override
//    public void displayUnFollowExceptionMessage(Exception e) {
//        view.displayErrorMessage("Failed to unfollow because of exception: " + e.getMessage());
//    }



    /**
     Follow Service
     */

    public void followUser(User user)
    {
        view.displayInfoMessage("Adding " + user.getName() + "...");
        new FollowService().follow(user, this);
    }

    @Override
    public void followSucceed(boolean val) {
        view.updateFollow(false);
    }

//    @Override
//    public void displayFollowFailedMessage(String message) {
//        view.displayFailedMessage("Failed to follow: " + message);
//    }
//
//
//    @Override
//    public void displayFollowErrorMessage(Exception e) {
//        view.displayErrorMessage("Failed to follow because of exception: " + e.getMessage());
//    }

    /**
    Post Status
     */
    public void postStatus(String post) throws ParseException {
        view.displayInfoMessage("Posting Status...");
        get().postStatus(post, this);
    }

    @Override
    public void postStatusSucceed(String message) {
        view.displayInfoMessage(message);
    }

//    @Override
//    public void postStatusFailed(String message) {
//        view.displayFailedMessage( "Failed to post status: " + message);
//    }
//
//    @Override
//    public void postStatusException(Exception e) {
//        view.displayFailedMessage( "Failed to post status because of exception: " + e.getMessage());
//    }

//    @Override
//    public void postStatusException(Exception e) {
//        view.displayExceptionMessage("Failed to post status because of exception: " + e.getMessage());
//    }

    /*
    Logout
     */

    public void logout()
    {
        view.displayInfoMessage("Logging Out...");
        new LogoutService().logout(this);
    }

    @Override
    public void logoutSucceed() {
        view.logout();
    }

    @Override
    public void logoutFailed(String message) {
        view.displayFailedMessage("Failed to logout: " + message);
    }

    @Override
    public void logoutException(Exception e) {
        view.displayExceptionMessage("Failed to logout because of exception: " + e.getMessage());
    }



    @Override
    public void handleFailure(String message) {
        view.displayFailedMessage(message);
        // DO NOTHING
    }

    @Override
    public void handleException(Exception exception) {
        view.displayExceptionMessage(exception.getMessage());
        // DO NOTHING
    }

    /**
     * For test
     */

    private PostStatusService postStatusService;
    private PostStatusService.PostStatusObserver observer;

    private StoryService storyService;

    public PostStatusService.PostStatusObserver getObserver()
    {
        return observer;
    }

    public PostStatusService get()
    {
        if(postStatusService == null)
        {
            postStatusService = new PostStatusService();
        }
        return postStatusService;
    }

    public StoryService getStoryService()
    {
        if(storyService == null)
        {
            storyService = new StoryService();
        }
        return storyService;
    }

//    public PostStatusService.PostStatusObserver getNewObserver()
//    {
//        return new PostStatusService.PostStatusObserver() {
//            @Override
//            public void postStatusSucceed(String message) {
//
//            }
//
//            @Override
//            public void handleFailure(String message) {
//
//            }
//
//            @Override
//            public void handleException(Exception exception) {
//
//            }
//        };
//    }

}
