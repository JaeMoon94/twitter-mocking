package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;

public class StoryRequest {
    private AuthToken authToken;
    private String userAlias;
    private int limit;
    private Status lastStory;

    private StoryRequest(){}

    public StoryRequest(AuthToken authToken, String userAlias, int limit, Status lastStory){
        this.authToken = authToken;
        this.userAlias = userAlias;
        this.limit = limit;
        this.lastStory = lastStory;
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

    public Status getLastStory() {
        return lastStory;
    }

    public void setLastStory(Status lastStory) {
        this.lastStory = lastStory;
    }
}
