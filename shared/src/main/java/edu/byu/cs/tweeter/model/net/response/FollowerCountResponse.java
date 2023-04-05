package edu.byu.cs.tweeter.model.net.response;

public class FollowerCountResponse extends Response{

    private int followerCount;


    public FollowerCountResponse(String message) {
        super(false, message);
    }

    public FollowerCountResponse(int followerCount) {
        super(true, null);
        this.followerCount = followerCount;
    }

    public int getFollowerCount()
    {
        return followerCount;
    }
}
