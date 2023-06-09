package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowerRequest;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of followers.
 */
public class GetFollowersTask extends PagedUserTask {

    static final String URL_PATH = "/getfollowers";
    public GetFollowersTask(AuthToken authToken, User targetUser, int limit, User lastFollower,
                            Handler messageHandler) {
        super(authToken, targetUser, limit, lastFollower, messageHandler);
    }

    @Override
    protected Pair<List<User>, Boolean> getItems() throws IOException, TweeterRemoteException {
        String targetUserAlias = targetUser == null ? null : targetUser.getAlias();
        String lastFollowerAlis = lastItem == null ? null : lastItem.getAlias();
        FollowerRequest request = new FollowerRequest(authToken,
                targetUserAlias,
                limit,
                lastFollowerAlis);
        FollowerResponse response = getServerFacade().getFollowers(request, URL_PATH);
        return new Pair<>(response.getFollowers(), response.getHasMorePages());
//        return getFakeData().getPageOfUsers(getLastItem(), getLimit(), getTargetUser());
    }
}
