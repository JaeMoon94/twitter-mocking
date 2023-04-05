package edu.byu.cs.tweeter.client.presenter;

import android.graphics.Bitmap;
import android.widget.ImageView;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.RegisterService;
import edu.byu.cs.tweeter.client.model.service.observer.LoginRegisterObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter implements LoginRegisterObserver {

    public interface registerView extends LoginRegisterView
    {
        void uploadImage();
    }

    private registerView view;
    public RegisterPresenter(registerView view)
    {
        this.view = view;
    }


    public void register(String firstName, String lastName, String alis, String password, Bitmap image)
    {
        String validateFail = validateRegistration(firstName, lastName, alis, password, image);
        if (validateFail == null)
        {
            view.clearInfoMessage();
            view.clearErrorMessage();
            new RegisterService(firstName, lastName, alis, password, image, this);
        }
        else
        {
            view.displayExceptionMessage(validateFail);
        }
    }


    public String validateRegistration(String firstName, String lastName, String alias, String password, Bitmap imageToUpload) {
        if (firstName.length() == 0) {
            return "First Name cannot be empty.";
        }
        if (lastName.length() == 0) {
           return "Last Name cannot be empty.";
        }
        if (alias.length() == 0) {
          return "Alias cannot be empty.";
        }
        if (alias.charAt(0) != '@') {
            return "Alias must begin with @.";
        }
        if (alias.length() < 2) {
            return  "Alias must contain 1 or more characters after the @.";
        }
        if (password.length() == 0) {
            return "Password cannot be empty.";
        }

        if (imageToUpload == null) {
            return "Profile image must be uploaded.";
        }
        return null;
    }

    public void uploadImage()
    {
        view.uploadImage();
    }


    /*
    Observer method override
     */

    @Override
    public void handleSuccess(User user, AuthToken token) {
        view.clearErrorMessage();
        view.clearInfoMessage();
        view.displayInfoMessage("Hello " + Cache.getInstance().getCurrUser().getName());
        view.navigateToUser(user, token);
    }

    @Override
    public void handleFailure(String message) {
        view.displayFailedMessage("Failed to register: " + message);
    }

    @Override
    public void handleException(Exception e) {
        view.displayExceptionMessage("Failed to register because of exception: " + e.getMessage());
    }



}
