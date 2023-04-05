package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that logs out a user (i.e., ends a session).
 */
public class LogoutTask extends AuthenticatedTask {

    static final String URL_PATH = "/logout";
    public LogoutTask(AuthToken authToken, Handler messageHandler) {
        super(authToken, messageHandler);
    }

    @Override
    protected void runTask() throws IOException, TweeterRemoteException {
        LogoutRequest request = new LogoutRequest(authToken);
        LogoutResponse response = getServerFacade().logout(request, URL_PATH);
        if(response.isSuccess())
        {
            sendSuccessMessage();
        }
        else{
            throw new RuntimeException("CANNOT LOGOUT!");
        }
//        LogoutRequest request = new LogoutRequest(authToken);
//        LogoutResponse response = getServerFacade().logout(request, URL_PATH);
//        return new Pair<>(response.isSuccess(), response.getMessage());
    }
}
