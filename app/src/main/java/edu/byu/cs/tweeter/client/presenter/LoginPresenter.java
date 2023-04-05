package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.LoginService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.LoginRegisterObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

// Presenter is Concrete Observer because it implements Login Observer.
public class LoginPresenter implements LoginRegisterObserver {
    private LoginRegisterView view;
    public LoginPresenter(LoginRegisterView view)
    {
        this.view = view;
    }
    // Methods that the view can call on the presenterz
    public void login(String usernName, String password){
        String validateError = validateLogin(usernName, password);
        if(validateError == null)
        {
            view.clearErrorMessage();
            view.displayInfoMessage("Loggin in...");
            new LoginService(usernName, password, this);
        }
        else{
            view.displayExceptionMessage(validateError);
        }

    }

    public String validateLogin(String alias, String password) {
        if (alias.charAt(0) != '@') {
            return "Alias must begin with @.";
        }
        if (alias.length() < 2) {
            return "Alias must contain 1 or more characters after the @.";
        }
        if (password.length() == 0) {
            return "Password cannot be empty.";
        }
        return null;
    }

    // methods that the presenter can call on the view

    @Override
    public void handleSuccess(User user, AuthToken token) {
        view.clearInfoMessage();
        view.clearErrorMessage();
        view.displayInfoMessage("Hello " + Cache.getInstance().getCurrUser().getName());
        view.navigateToUser(user,token);
    }

    @Override
    public void handleFailure(String message) {
        view.displayFailedMessage("Failed to login: " + message);
    }

    @Override
    public void handleException(Exception e) {
        view.displayExceptionMessage("Failed to login because of exception: " + e.getMessage());
    }


}
