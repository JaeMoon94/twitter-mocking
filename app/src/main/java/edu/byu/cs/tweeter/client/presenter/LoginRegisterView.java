package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public interface LoginRegisterView extends View{
    void navigateToUser(User user, AuthToken token);
    void clearInfoMessage();
    void clearErrorMessage();
}
