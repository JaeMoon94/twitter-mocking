package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of other users being followed by a specified user.
 */
public class GetFollowingTask extends PagedUserTask {

    static final String URL_PATH = "/getFollowing";

    public GetFollowingTask(AuthToken authToken, User targetUser, int limit, User lastFollowee,
                            Handler messageHandler) {
        super(authToken, targetUser, limit, lastFollowee, messageHandler);
    }

    @Override
    protected Pair<List<User>, Boolean> getItems() throws IOException, TweeterRemoteException {
        String targetUserAlias = targetUser == null ? null : targetUser.getAlias();
        String lastFolloweeAlias = lastItem == null ? null : lastItem.getAlias();
        FollowingRequest request = new FollowingRequest(authToken,
                targetUserAlias,
                getLimit(),
                lastFolloweeAlias);
        FollowingResponse response = getServerFacade().getFollowees(request,
                URL_PATH);
        return new Pair<>(response.getFollowees(), response.getHasMorePages());
//        return getFakeData().getPageOfUsers(getLastItem(), getLimit(), getTargetUser());
    }
}
