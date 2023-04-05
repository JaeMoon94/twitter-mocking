package edu.byu.cs.tweeter.server.service;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.server.dao.facotryImplement.FollowDAOImp;
import edu.byu.cs.tweeter.server.dao.facotryImplement.StatusDAOImp;
import edu.byu.cs.tweeter.server.dao.factory.Factory;
import edu.byu.cs.tweeter.server.dao.factory.FactoryProvider;

public class Testfile {
    public static void main(String[] args) throws Exception {
        Factory factory = new FactoryProvider();
//        FollowDAOImp myimp = new FollowDAOImp();
////        myimp.getFollowees(new FollowingRequest(new AuthToken("05a89e89-9e00-4000-b491-bc37781f0c2b"), "@Allen", 10, null));
////        StatusDAOImp myimp = new StatusDAOImp();
////        System.out.println(myimp.getOwnerAlias(new FeedRequest(new AuthToken("05a89e89-9e00-4000-b491-bc37781f0c2b"),"@a",10,null)));
//        myimp.follow(new User("Allen",
//                "Anderson",
//                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png",
//                "@allen"),new User("test", "test_Last","@aa","https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png"));
//        FollowService service = new FollowService(factory);
//        service.follow(new FollowRequest(new User("Allen",
//                "Anderson",
//                "@allen",
//                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png"),
//                new AuthToken("dee427bc-0cce-4021-a18e-a60fd358e853","2022.11.19.10.24.07")));
//        myimp.isFollower(new IsFollowerRequest(new User("test9",
//                "test9_lastName",
//                "@a9",
//                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png"),
//                new User("Allen",
//                        "Anderson",
//                        "@allen",
//                        "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png"),
//                new AuthToken()));
//        service.isFollower(new IsFollowerRequest(new User("test9",
//                "test9_lastName",
//                "@a9",
//                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png"),
//                new User("Allen",
//                        "Anderson",
//                        "@allen",
//                        "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png"),
//                new AuthToken()));
//        factory.returnImageDAO().upload("https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png","@allen");

//        List<User> testUsers = new ArrayList<>();
//        for(int i = 0; i < 10000; i++)
//        {
//            String firstName = "User" + i;
//            String lsatName = "Batch" + i;
//            String alias = "@bird" + i;
//            String imageUrl = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png";
//            User user = new User(firstName,lsatName,alias,imageUrl);
//            testUsers.add(user);
//        }
//
//        factory.returnUserDAO().addUserBatch(testUsers);
//        List<String> testUserAlias = new ArrayList<>();
//        for(int i = 0; i < 10000; i ++)
//        {
//            String alias = "@bird" + i;
//            testUserAlias.add(alias);
//        }
//
//        factory.returnFollowDAO().addFollowBatch(testUserAlias);



    }
}
