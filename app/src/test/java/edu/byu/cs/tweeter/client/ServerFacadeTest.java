package edu.byu.cs.tweeter.client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowerCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowerRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.FollowerCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.util.FakeData;

public class ServerFacadeTest {
    private ServerFacade serverFacade;
    private User user;
    private FakeData fakeData;

    @BeforeEach
    public void setup()
    {
        serverFacade = new ServerFacade();
        user = new User("Allen", "Anderson", "@allen", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        fakeData = FakeData.getInstance();
    }
    @Test
    public void testRegister() throws IOException, TweeterRemoteException {
        String urlPath = "/register";
        RegisterResponse response = null;
        response = serverFacade.register(new RegisterRequest("firstName", "lastName", "username", "password", "image"), urlPath);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getAuthToken());
        Assertions.assertEquals(user, response.getUser());
        response = serverFacade.register(new RegisterRequest(null, "lastName", "username", "password", "image"), urlPath);
        Assertions.assertFalse(response.isSuccess());
    }

    @Test
    public void testGetFollowers() throws IOException, TweeterRemoteException {
        String urlPath = "/getfollowers";
        FollowerRequest request = new FollowerRequest(new AuthToken(), "@aa", 10, "@lastAlias");
        FollowerResponse response = serverFacade.getFollowers(request, urlPath);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getFollowers());
        Assertions.assertTrue(response.getFollowers().size() == 10);
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertTrue(response.getFollowers().contains(fakeData.getFakeUsers().get(0)));
    }

    @Test
    public void testGetFollowersCount() throws IOException, TweeterRemoteException {
        String urlPath = "/getFollowerCount";
        FollowerCountRequest request = new FollowerCountRequest(new AuthToken(), user.getAlias());
        FollowerCountResponse response = serverFacade.getFollowerCount(request, urlPath);
        Assertions.assertNotNull(response);
        Integer testCount = fakeData.getFakeUsers().size();
        Assertions.assertEquals(testCount, response.getFollowerCount());
    }

    @Test
    public void testGetFollowingCount() throws IOException, TweeterRemoteException {
        String urlPath = "/getFollowingCount";
        FollowingCountRequest request = new FollowingCountRequest(new AuthToken(), user.getAlias());
        FollowingCountResponse response = serverFacade.getFollowingCount(request, urlPath);
        Assertions.assertNotNull(response);
        Integer testCount = fakeData.getFakeUsers().size();
        Assertions.assertEquals(testCount, response.getFollowingCount());
    }

}


