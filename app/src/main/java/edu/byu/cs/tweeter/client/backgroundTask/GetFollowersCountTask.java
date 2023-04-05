package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowerCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowerCountResponse;

/**
 * Background task that queries how many followers a user has.
 */
public class GetFollowersCountTask extends GetCountTask {

    static final String URL_PATH = "/getFollowerCount";
    public GetFollowersCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(authToken, targetUser, messageHandler);
    }

    @Override
    protected int runCountTask() throws IOException, TweeterRemoteException {
        FollowerCountRequest request = new FollowerCountRequest(authToken,
                getTargetUser().getAlias());
        FollowerCountResponse response = getServerFacade().getFollowerCount(request,
                URL_PATH);
        if(response.isSuccess())
        {
            return response.getFollowerCount();
        }
        else{
            throw new RuntimeException("Error occur Getting Follower Count");
        }
//        return 20;
    }
}
