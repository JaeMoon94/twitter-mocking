package edu.byu.cs.tweeter.server.dao.facotryImplement;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class UserDAOImp implements UserDAO {

//    AmazonDynamoDB client
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
        .withRegion("us-east-2")
        .build();

        DynamoDB dynamoDB = new DynamoDB(client);
        Table userTable = dynamoDB.getTable("user");
        Table authTokenTable = dynamoDB.getTable("authToken");
        private final static Logger logger = Logger.getLogger(UserDAOImp.class.toString());


    @Override
    public User getuser(String alias) {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("user_alias",alias);
        Item userOutcome = userTable.getItem(spec);
        User user =  new User(userOutcome.getString("firstName"),
                userOutcome.getString("lastName"),
                userOutcome.getString("user_alias"),
                userOutcome.getString("imageURL"));
        return user;
    }

    @Override
    public User RegisterUser(String userAlias, String firstName, String lastName, String password, String imgURL) throws Exception {

        String salt = getSalt();
        String hashedPassword = getSecurePassword(password, salt);

        try {
            PutItemOutcome outcome = userTable.putItem(new Item().withPrimaryKey("user_alias", userAlias)
                    .withString("firstName", firstName)
                    .withString("lastName", lastName)
                    .withString("password", hashedPassword)
                    .withString("imageURL", imgURL)
                    .withInt("followerCount", 0)
                    .withInt("followingCount", 0)
                    .withString("salt",salt));
        }catch (Exception e)
        {
            throw new Exception("[Internal Server Error] User not available to register");
        }
        //"[Internal Server Error]" User not available to register
//        PutItemOutcome authTokenOutcome = authTokenTable.putItem(new Item().withPrimaryKey("authToken", authToken).withString("dateTime", authToken.getDatetime()));
        User user = new User(firstName,lastName,userAlias,imgURL);
        return user;
    }

    @Override
    public AuthToken getAuthToken(String alias) {
        AuthToken authToken;
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Timestamp(System.currentTimeMillis()));
        authToken = new AuthToken(String.valueOf(UUID.randomUUID()), timeStamp);

        PutItemOutcome authTokenOutcome = authTokenTable.putItem(new Item().withPrimaryKey("authToken", authToken.getToken())
                .withString("dateTime", timeStamp)
                .withString("alias",alias));
        return authToken;
    }

    @Override
    public User login(String userAlias, String password) throws Exception {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("user_alias",userAlias);
        User user;
//        AuthToken authToken = null;
        try
        {
            Item result = userTable.getItem(spec);
            if(result == null) throw new Exception("User not exist");
            String userPassword = getSecurePassword(password, result.getString("salt"));
            if(!result.getString("password").equals(userPassword)){ throw new Exception("Incorrect Password");}
            user = new User(result.getString("firstName"),
                    result.getString("lastName"),
                    result.getString("user_alias"),
                    result.getString("imageURL"));
//            authToken = new AuthToken(String.valueOf(UUID.randomUUID()));
//            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Timestamp(System.currentTimeMillis()));
//            PutItemOutcome authTokenOutcome = authTokenTable.putItem(new Item().withPrimaryKey("authToken", authToken.getToken())
//                    .withString("dateTime", timeStamp));
        }catch (Exception e)
        {
            throw new Exception("[Internal Server Error] User not available to login");
        }
        return user;
    }

    @Override
    public void logout(AuthToken authToken) throws Exception {
        DeleteItemSpec deleteItemSpec = new DeleteItemSpec().
                withPrimaryKey(new PrimaryKey("authToken", authToken.getToken()));
        try{
            authTokenTable.deleteItem(deleteItemSpec);
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            throw new Exception("[Internal Server Error] Not available to delete AuthToken");

        }
    }

    @Override
    public User getUserWithToken(AuthToken authToken) {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("authToken", authToken.getToken());
        System.out.println("Token: " + authToken);
        Item tokenTableOutcome = authTokenTable.getItem(spec);
        String alias = tokenTableOutcome.getString("alias");
        User user = getuser(alias);
        System.out.println("Current User: " + user.toString());
        return user;
    }

    @Override
    public List<User> getUsers(List<String> userAlias) {
        List<User> userList = new ArrayList<>();

        for(String userAlis : userAlias)
        {
            userList.add(getuser(userAlis));
        }
        return userList;
    }

    @Override
    public Integer getFollowingCount(User targetUser) {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("user_alias",targetUser.getAlias());
        Item outcome = userTable.getItem(spec);
        return outcome.getInt("followingCount");
    }

    @Override
    public Integer getFollowerCount(User targetUser) {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("user_alias",targetUser.getAlias());
        Item outcome = userTable.getItem(spec);
        return outcome.getInt("followerCount");
    }

    @Override
    public void updateCount(User user, Integer count, boolean iterate, String countType) throws Exception {
        UpdateItemSpec spec = getUpdateItemSpec(user, count, iterate, countType);
        try
        {
            UpdateItemOutcome outcome = userTable.updateItem(spec);
        }catch (Exception e)
        {
            System.out.println("updateCount Error: " + e.getMessage());
            throw new Exception("[Internal Server Error] Not available to updateCount");
        }

    }

    @Override
    public void addUserBatch(List<User> users) {
        TableWriteItems items = new TableWriteItems(userTable.getTableName());

        for (User user : users)
        {
            Item item = new Item()
                    .withPrimaryKey("user_alias", user.getAlias())
                    .withString("firstName" , user.getFirstName())
                    .withString("lastName", user.getLastName())
                    .withString("imageURL", user.getImageUrl())
                    .withInt("followerCount", 0)
                    .withInt("followingCount", 0);
            items.addItemToPut(item);

            if (items.getItemsToPut() != null && items.getItemsToPut().size() == 25) {
                loopBatchWrite(items);
                items = new TableWriteItems(userTable.getTableName());
            }
        }
        if (items.getItemsToPut() != null && items.getItemsToPut().size() > 0) {
            loopBatchWrite(items);
        }

    }

    private void loopBatchWrite(TableWriteItems items) {

        if(items.getItemsToPut().size() > 25)
        {
            throw new RuntimeException("Too many items");
        }
        // The 'dynamoDB' object is of type DynamoDB and is declared statically in this example
        BatchWriteItemOutcome outcome = dynamoDB.batchWriteItem(items);
        logger.info("Wrote User Batch");

        // Check the outcome for items that didn't make it onto the table
        // If any were not added to the table, try again to write the batch
        while (outcome.getUnprocessedItems().size() > 0) {
            Map<String, List<WriteRequest>> unprocessedItems = outcome.getUnprocessedItems();
            outcome = dynamoDB.batchWriteItemUnprocessed(unprocessedItems);
            logger.info("Wrote more Users");
        }
    }

    private UpdateItemSpec getUpdateItemSpec(User currentUser, Integer count, boolean iterate, String countType) {
        if (iterate) count = count + 1; else count--;
        return new UpdateItemSpec().withPrimaryKey("user_alias", currentUser.getAlias())
                .withUpdateExpression("set " + countType + " = :r")
                .withValueMap(new ValueMap().withInt(":r", count))
                .withReturnValues(ReturnValue.UPDATED_NEW);
    }
//
//    @Override
//    public void logout() {
//
//    }
//
//    @Override
//    public void initiateData(List<User> userList) {
//
//    }

    private static String getSalt() throws NoSuchAlgorithmException, NoSuchProviderException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt.toString();
    }

    private static String getSecurePassword(String passwordToHash, String salt) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            md.update(salt.getBytes());

            byte[] bytes = md.digest(passwordToHash.getBytes());

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
                        .substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }


}
