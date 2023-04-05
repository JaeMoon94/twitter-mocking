package edu.byu.cs.tweeter.model.net.response;

public class FollowingCountResponse extends Response{

    private int followingCount;

    public FollowingCountResponse(String message) {
        super(false, message);
    }

    public FollowingCountResponse(int followingCount) {
        super(true, null);
        this.followingCount = followingCount;
    }


    public int getFollowingCount() {
        return followingCount;
    }
}
