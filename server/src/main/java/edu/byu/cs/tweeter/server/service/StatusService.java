package edu.byu.cs.tweeter.server.service;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.FollowQueueRequest;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowerRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.FollowQueueResponse;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.factory.Factory;
import edu.byu.cs.tweeter.server.dao.factory.FactoryProvider;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

public class StatusService {

    Factory factory;

    public StatusService(Factory factory)
    {
        this.factory = factory;
    }

    public FeedResponse getFeeds(FeedRequest request) throws Exception {
        if(request.getUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        } else if(request.getAuthToken() == null)        {
            throw new RuntimeException("[Bad Request] Request needs to have a authtoken");
        }
//        Pair<List<Status>, Boolean> feeds = getFakeData().getPageOfStatus(request.getLastFeed(), request.getLimit());
        Pair<List<Status>, Boolean> feeds = factory.returnStatusDAO().getFeed(request);
        return new FeedResponse(feeds.getFirst(),feeds.getSecond());
    }

    public StoryResponse getStories(StoryRequest request) throws Exception {
        if(request.getUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a User alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        else if(request.getAuthToken() == null){
            throw new RuntimeException("[Bad Request] Request needs to have a authtoken");
        }
        Pair<List<Status>, Boolean> stories = factory.returnStatusDAO().getStory(request,factory.returnUserDAO().getuser(request.getUserAlias()));
        return new StoryResponse(stories.getFirst(),stories.getSecond());
    }

    public PostStatusResponse postStatus(PostStatusRequest request) throws Exception {
        if(request.getStatus() == null){
            throw new RuntimeException("[Bad Request] Request needs to have a status");
        }
        else if(request.getAuthToken() == null)
        {
            throw new RuntimeException("[Bad Request] Missing a AuthToken");
        }
        factory.returnStatusDAO().postStatus(request, factory.returnFollowDAO().getFollowerAlias(request.getStatus().getUser()));
        factory.returnDTO().addPostToQueue(request);
        return new PostStatusResponse();
    }

    public FollowQueueResponse triggerSecondQeueAndFetchFollowers(PostStatusRequest request) throws Exception {
        FollowerRequest followerRequest = new FollowerRequest(request.getAuthToken(),
                request.getStatus().getUser().getAlias(),
                100,null);

        Pair<List<String>, Boolean> followerResponse = null;
        FollowQueueResponse response = null;

        int num = 0;
        do{
            followerResponse = factory.returnFollowDAO().getFollowers(followerRequest);
            FollowQueueRequest queueRequest = new FollowQueueRequest(request.getAuthToken(), followerResponse.getFirst(), request.getStatus());
            response = factory.returnDTO().addFollowerToQueue(queueRequest);

            String lastFollowerAlias = (followerResponse.getFirst().size() > 0) ?
                    followerResponse.getFirst().get(followerResponse.getFirst().size() - 1) : null;

            followerRequest.setLastFollowerAlias(lastFollowerAlias);
            System.out.println("Fetching follower counter : " + num);
            num++;

        }while(followerResponse.getSecond());

        return response;
    }

    public FollowQueueResponse updateFeedWithStatuses(FollowQueueRequest request)
    {
        factory.returnStatusDAO().addFeedBatch(request.getUserAlias(), request.getStatus());
        return new FollowQueueResponse(true);
    }

    FakeData getFakeData() {
        return FakeData.getInstance();
    }
}
