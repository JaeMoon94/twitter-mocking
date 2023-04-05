package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.net.request.FollowQueueRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.FollowQueueResponse;

public interface DTO {
    boolean addPostToQueue(PostStatusRequest request);
    FollowQueueResponse addFollowerToQueue(FollowQueueRequest request);
}
