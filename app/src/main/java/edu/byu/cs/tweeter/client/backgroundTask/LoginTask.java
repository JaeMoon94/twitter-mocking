package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that logs in a user (i.e., starts a session).
 */
public class LoginTask extends AuthenticateTask {

    private static final String LOG_TAG = "LoginTask";
    static final String URL_PATH = "/login";

//    User loggedInUser;
//    AuthToken authToken;

    public LoginTask(String username, String password, Handler messageHandler) {
        super(messageHandler, username, password);
    }

    @Override
    protected Pair<User, AuthToken> runAuthenticationTask() throws IOException, TweeterRemoteException {
        LoginRequest request = new LoginRequest(username, password);
        LoginResponse response = getServerFacade().login(request, URL_PATH);
//        loggedInUser = response.getUser();
//        authToken = response.getAuthToken();

        try {
            if (response.isSuccess()) {
                return new Pair<>(response.getUser(), response.getAuthToken());
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            sendExceptionMessage(e);

        }
        return null;
    }


//        return new Pair<>(loggedInUser, authToken);
}
