package edu.byu.cs.tweeter.model.net.response;

public class FollowQueueResponse extends Response{

    public FollowQueueResponse(boolean success) {
        super(success);
    }

    public FollowQueueResponse(boolean success, String message) {
        super(success, message);
    }
}
