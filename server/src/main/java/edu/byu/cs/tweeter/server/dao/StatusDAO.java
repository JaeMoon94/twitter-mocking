package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.util.Pair;

public interface StatusDAO {
    Pair<List<Status>, Boolean> getFeed(FeedRequest request) throws Exception;
    Pair<List<Status>, Boolean> getStory(StoryRequest request, User user) throws Exception;
    void postStatus(PostStatusRequest request, List<String> followerAlias) throws Exception;
    String getOwnerAlias(FeedRequest request);
    void addFeedBatch(List<String> followerAlias, Status status);

}
