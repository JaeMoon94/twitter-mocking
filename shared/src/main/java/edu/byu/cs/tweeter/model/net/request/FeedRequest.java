package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;

public class FeedRequest {
    private AuthToken authToken;
    private String userAlias;
    private int limit;
    private Status lastFeed;

    private FeedRequest() {}

    public FeedRequest(AuthToken authToken, String userAlias, int limit, Status lastFeed) {
        this.authToken = authToken;
        this.userAlias = userAlias;
        this.limit = limit;
        this.lastFeed = lastFeed;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Status getLastFeed() {
        return lastFeed;
    }

    public void setLastFeed(Status lastFeed) {
        this.lastFeed = lastFeed;
    }
}
