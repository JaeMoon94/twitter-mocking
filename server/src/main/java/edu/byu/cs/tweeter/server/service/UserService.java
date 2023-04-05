package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.server.dao.factory.Factory;
import edu.byu.cs.tweeter.util.FakeData;

public class UserService {

    Factory factory;

    public UserService(Factory factory)
    {
        this.factory = factory;
    }

    public LoginResponse login(LoginRequest request) throws Exception {
        if(request.getUsername() == null){
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if(request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        }

        // TODO: Generates dummy data. Replace with a real implementation.
        User user = factory.returnUserDAO().login(request.getUsername(), request.getPassword());
        AuthToken authToken = factory.returnUserDAO().getAuthToken(request.getUsername());
        return new LoginResponse(user, authToken);
    }

    public LogoutResponse logout(LogoutRequest request) throws Exception {
        if(request.getAuthToken() == null)
        {
            throw new RuntimeException("[Bad Request] Missing a AuthToken");
        }
        factory.returnUserDAO().logout(request.getAuthToken());
        return new LogoutResponse();
    }

    public RegisterResponse register(RegisterRequest request) throws Exception {
        if(request.getAlias() == null){
            throw new RuntimeException("[Bad Request] Missing a alias");
        } else if(request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        } else if(request.getFirstName() == null) {
            throw new RuntimeException("[Bad Request] Missing a first name");
        } else if(request.getLastName() == null) {
            throw new RuntimeException("[Bad Request] Missing a last name");
        } else if(request.getImageUrl() == null) {
            throw new RuntimeException("[Bad Request] Missing an image URL");
        }

        //todo : Upload image and reterieve URL and swap with request.getimgURL
        request.setImageUrl(factory.returnImageDAO().upload(request.getImageUrl(), request.getAlias()));
        factory.returnUserDAO().RegisterUser(request.getAlias(),
                request.getFirstName(),
                request.getLastName(),
                request.getPassword(),
                request.getImageUrl());
        User user = factory.returnUserDAO().getuser(request.getAlias());
        AuthToken authToken = factory.returnUserDAO().getAuthToken(request.getAlias());
        return new RegisterResponse(user,authToken);
    }

    public GetUserResponse getUser(String alias)
    {
        if(alias == null)
        {
            throw new RuntimeException("[Bad Request] Missing a alias");
        }
        User user = factory.returnUserDAO().getuser(alias);
        return new GetUserResponse(user);
    }

    /**
     * Returns the dummy user to be returned by the login operation.
     * This is written as a separate method to allow mocking of the dummy user.
     *
     * @return a dummy user.
     */
    User getDummyUser() {
        return getFakeData().getFirstUser();
    }

    /**
     * Returns the dummy auth token to be returned by the login operation.
     * This is written as a separate method to allow mocking of the dummy auth token.
     *
     * @return a dummy auth token.
     */
    AuthToken getDummyAuthToken() {
        return getFakeData().getAuthToken();
    }

    /**
     * Returns the {@link FakeData} object used to generate dummy users and auth tokens.
     * This is written as a separate method to allow mocking of the {@link FakeData}.
     *
     * @return a {@link FakeData} instance.
     */
    FakeData getFakeData() {
        return FakeData.getInstance();
    }
}
