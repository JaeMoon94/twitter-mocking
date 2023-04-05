package edu.byu.cs.tweeter.model.net.request;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;

public class FollowQueueRequest {

    private AuthToken authToken;
    private List<String> userAlias;
    private Status status;

    public FollowQueueRequest()
    {}

    public FollowQueueRequest(AuthToken authToken, List<String> userAlias, Status status) {
        this.authToken = authToken;
        this.status = status;
        this.userAlias = userAlias;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public List<String> getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(List<String> userAlias) {
        this.userAlias = userAlias;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
