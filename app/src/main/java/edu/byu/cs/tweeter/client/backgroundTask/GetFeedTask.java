package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's feed.
 */
public class GetFeedTask extends PagedStatusTask {

    static final String URL_PATH = "/getFeeds";

    public GetFeedTask(AuthToken authToken, User targetUser, int limit, Status lastStatus,
                       Handler messageHandler) {
        super(authToken, targetUser, limit, lastStatus, messageHandler);
    }


    @Override
    protected Pair<List<Status>, Boolean> getItems() throws IOException, TweeterRemoteException {
        String targetUserAlias = targetUser == null ? null : targetUser.getAlias();
        Status lastStauts = lastItem == null ? null : getLastItem();
//        if(getLastItem() == null){
//            lastItem = getFakeData().getFakeStatuses().get(0);
//        }
        FeedRequest request = new FeedRequest(authToken,
                targetUserAlias,
                getLimit(),
                lastStauts);
        FeedResponse response = getServerFacade().getFeeds(request,URL_PATH);
//        return getFakeData().getPageOfStatus(getLastItem(), getLimit());
        return new Pair<>(response.getFeeds(), response.getHasMorePages());
    }
}
