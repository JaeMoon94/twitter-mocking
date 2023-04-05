package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.document.Table;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;

public interface UserDAO {
    User getuser(String alias);
    User RegisterUser(String userAlias, String firstName, String lastName, String password, String imgURL) throws Exception;
    AuthToken getAuthToken(String alias);
    User login(String userAlias, String password) throws Exception;
    void logout(AuthToken authToken) throws Exception;
    User getUserWithToken(AuthToken token);
    List<User> getUsers(List<String> users);
    Integer getFollowingCount(User targetUser);
    Integer getFollowerCount(User targetUser);
    void updateCount(User user, Integer count, boolean iterate, String countType) throws Exception;
    void addUserBatch(List<User> users);

}
