package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;

/**
 * Background task that queries how many other users a specified user is following.
 */
public class GetFollowingCountTask extends GetCountTask {

    static final String URL_PATH = "/getFollowingCount";

    public GetFollowingCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(authToken, targetUser, messageHandler);
    }

    @Override
    protected int runCountTask() throws IOException, TweeterRemoteException {
        FollowingCountRequest request = new FollowingCountRequest(authToken,
                getTargetUser().getAlias());
        FollowingCountResponse response = getServerFacade().getFollowingCount(request, URL_PATH);
        if(response.isSuccess())
        {
            return response.getFollowingCount();
        }
        else
        {
            throw new RuntimeException("Error occur Getting Following Count");
        }
//        return 20;
    }
}
