package edu.byu.cs.tweeter.server.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowerCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowerRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.UnFollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowerCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.UnFollowResponse;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.server.dao.factory.Factory;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService {

    Factory factory;
    public FollowService(Factory factory)
    {
        this.factory = factory;
    }
    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link FollowDAO} to
     * get the followees.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(FollowingRequest request) throws Exception {
        if(request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        } else if(request.getAuthToken() == null)        {
            throw new RuntimeException("[Bad Request] Request needs to have a authtoken");
        }
        Pair<List<String>,Boolean> responsePair = factory.returnFollowDAO().getFollowees(request);

        return new FollowingResponse(factory.returnUserDAO().getUsers(responsePair.getFirst()),responsePair.getSecond());
    }

    public FollowerResponse getFollowers(FollowerRequest request) throws Exception {
        if(request.getFollowingAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a following alias");
        }else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }else if(request.getAuthToken() == null)        {
            throw new RuntimeException("[Bad Request] Request needs to have a authtoken");
        }
//        User lastUser = getFakeData().findUserByAlias(request.getLastFollowerAlias());
//        User targetUser = getFakeData().findUserByAlias(request.getFollowingAlias());
//        Pair<List<User>, Boolean> followers = getFakeData().getPageOfUsers(lastUser, request.getLimit(), targetUser);
        Pair<List<String>, Boolean> followers = factory.returnFollowDAO().getFollowers(request);
        return new FollowerResponse(factory.returnUserDAO().getUsers(followers.getFirst()), followers.getSecond());
    }

    public FollowingCountResponse getFollowingCount(FollowingCountRequest request)
    {
        if(request.getTargetAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a target alias");
        }else if(request.getAuthToken() == null)        {
            throw new RuntimeException("[Bad Request] Request needs to have a authtoken");
        }
        User user = factory.returnUserDAO().getuser(request.getTargetAlias());
        int count = factory.returnUserDAO().getFollowingCount(user);
        return new FollowingCountResponse(count);
    }


    public FollowerCountResponse getFollowerCount(FollowerCountRequest request)
    {
        if(request.getTargetAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a target alias");
        }else if(request.getAuthToken() == null)        {
            throw new RuntimeException("[Bad Request] Request needs to have a authtoken");
        }
        User user = factory.returnUserDAO().getuser(request.getTargetAlias());
        int count = factory.returnUserDAO().getFollowerCount(user);
        return new FollowerCountResponse(count);
    }

    public FollowResponse follow(FollowRequest request) throws Exception {
        User currentUser = factory.returnUserDAO().getUserWithToken(request.getAuthToken());
        System.out.println("HELLO!");
        System.out.println("TargetUser Alias: " + request.getTargetUser().getAlias());
        if(request.getTargetUser()== null){
            throw new RuntimeException("[Bad Request] Request needs to have a target User");
        }else if(request.getAuthToken() == null)        {
            throw new RuntimeException("[Bad Request] Request needs to have a authtoken");
        }
        factory.returnFollowDAO().
                follow(request.getTargetUser(),
                        currentUser);

        Integer followingCount = factory.returnUserDAO().getFollowingCount(currentUser);
        factory.returnUserDAO().updateCount(currentUser, followingCount, true, "followingCount");

        Integer followerCount = factory.returnUserDAO().getFollowerCount(request.getTargetUser());
        factory.returnUserDAO().updateCount(request.getTargetUser(), followerCount, true, "followerCount");

//        factory.returnFollowDAO().
//                follow(request.getTargetUser(),
//                currentUser);
        return new FollowResponse();
    }

    public UnFollowResponse unFollow(UnFollowRequest request) throws Exception {
        User currentUser = factory.returnUserDAO().getUserWithToken(request.getAuthToken());
        if(request.getTargetUser()== null){
            throw new RuntimeException("[Bad Request] Request needs to have a target User");
        }else if(request.getAuthToken() == null)        {
            throw new RuntimeException("[Bad Request] Request needs to have a authtoken");
        }

        factory.returnFollowDAO().unFollow(request.getTargetUser(),
                currentUser);

        Integer followingCount = factory.returnUserDAO().getFollowingCount(currentUser);
        factory.returnUserDAO().updateCount(currentUser, followingCount, false, "followingCount");

        Integer followerCount = factory.returnUserDAO().getFollowerCount(request.getTargetUser());
        factory.returnUserDAO().updateCount(request.getTargetUser(), followerCount, false, "followerCount");


        return new UnFollowResponse();
    }

    public IsFollowerResponse isFollower(IsFollowerRequest request) throws Exception {
        if(request.getFollower() == null)
        {
            throw new RuntimeException("[Bad Request] Request needs to have a Follower");
        }

        else if(request.getFollowee() == null)
        {
            throw new RuntimeException("[Bad Request] Request needs to have a Followee");
        }else if(request.getAuthToken() == null)        {
            throw new RuntimeException("[Bad Request] Request needs to have a authtoken");
        }
        boolean isFollower = factory.returnFollowDAO().isFollower(request);
        return new IsFollowerResponse(isFollower);
    }


    /**
     * Returns an instance of {@link FollowDAO}. Allows mocking of the FollowDAO class
     * for testing purposes. All usages of FollowDAO should get their FollowDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
//    FollowDAO getFollowingDAO() {
//        return new FollowDAO();
//    }

    FakeData getFakeData() {
        return FakeData.getInstance();
    }
}
