package edu.byu.cs.tweeter.server.dao.factory;

import edu.byu.cs.tweeter.server.dao.DTO;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.server.dao.ImageDAO;
import edu.byu.cs.tweeter.server.dao.StatusDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.server.dao.facotryImplement.DTOImp;
import edu.byu.cs.tweeter.server.dao.facotryImplement.FollowDAOImp;
import edu.byu.cs.tweeter.server.dao.facotryImplement.ImageDAOImp;
import edu.byu.cs.tweeter.server.dao.facotryImplement.StatusDAOImp;
import edu.byu.cs.tweeter.server.dao.facotryImplement.UserDAOImp;

public class FactoryProvider implements Factory{


    @Override
    public FollowDAO returnFollowDAO() {
        return new FollowDAOImp();
    }

    @Override
    public ImageDAO returnImageDAO() {
        return new ImageDAOImp();
    }

    @Override
    public StatusDAO returnStatusDAO() {
        return new StatusDAOImp();
    }

    @Override
    public UserDAO returnUserDAO() {
        return new UserDAOImp();
    }

    @Override
    public DTO returnDTO() {
        return new DTOImp();
    }
}
