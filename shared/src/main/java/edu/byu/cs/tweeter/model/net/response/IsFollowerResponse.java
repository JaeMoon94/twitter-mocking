package edu.byu.cs.tweeter.model.net.response;

public class IsFollowerResponse extends Response{

    private boolean isFollower;

    public IsFollowerResponse(boolean isFollower) {
        super(true, null);
        this.isFollower = isFollower;
    }

    public IsFollowerResponse(String message) {
        super(false, message);
    }

    public boolean getIsFollower() {
        return isFollower;
    }

    public void setIsFollower(boolean follower) {
        isFollower = follower;
    }
}
