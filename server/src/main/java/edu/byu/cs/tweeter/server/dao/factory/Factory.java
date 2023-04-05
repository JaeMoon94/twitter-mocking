package edu.byu.cs.tweeter.server.dao.factory;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.DTO;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.server.dao.ImageDAO;
import edu.byu.cs.tweeter.server.dao.StatusDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.util.Pair;

public interface Factory {
    FollowDAO returnFollowDAO();
    ImageDAO returnImageDAO();
    StatusDAO returnStatusDAO();
    UserDAO returnUserDAO();
    DTO returnDTO();

}
