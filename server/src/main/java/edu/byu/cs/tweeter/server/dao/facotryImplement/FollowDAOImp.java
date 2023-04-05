package edu.byu.cs.tweeter.server.dao.facotryImplement;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowerRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.server.dao.factory.Factory;
import edu.byu.cs.tweeter.server.dao.factory.FactoryProvider;
import edu.byu.cs.tweeter.util.Pair;

public class FollowDAOImp implements FollowDAO{
    AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withRegion("us-east-2")
            .build();

    DynamoDB dynamoDB = new DynamoDB(client);
    Table followTable = dynamoDB.getTable("follow_tweeter");
    private final static Logger logger = Logger.getLogger(FollowDAOImp.class.toString());



    @Override
    public void follow(User targetUser, User currentUser) throws Exception {
        try{
            System.out.println("targetUser :" + targetUser.toString() );
            System.out.println("currentUser :" + currentUser.toString());
            System.out.println(followTable.getTableName());
            PutItemOutcome outcome = followTable.putItem(new Item()
                    .withPrimaryKey("followee_alias",targetUser.getAlias(),"follower_alias",currentUser.getAlias()));
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            throw new Exception("[Internal Server Error] Not available to follow");
        }
    }

    @Override
    public void unFollow(User targetUser, User currentUser) throws Exception {
        DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                .withPrimaryKey(new PrimaryKey("followee_alias",targetUser.getAlias(),
                        "follower_alias",currentUser.getAlias()));
        try {
            followTable.deleteItem(deleteItemSpec);
        }
        catch (Exception e)
        {
            throw new Exception("[Internal Server Error] Not available to unfollow");
        }

    }

    @Override
    public Pair<List<String>, Boolean> getFollowees(FollowingRequest request) throws Exception {
        assert request.getLimit() > 0;
        assert request.getFollowerAlias() != null;

        boolean hasMorePages = false;

//        List<User> allFollowees = getDummyFollowees();
        List<String> responseFollowees = new ArrayList<>();
        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":follower",request.getFollowerAlias());

        Iterator<Item> iterator;
        Item item;
        ItemCollection<QueryOutcome> users;


        QuerySpec spec = new QuerySpec().withKeyConditionExpression("follower_alias = :follower")
                .withValueMap(valueMap)
                .withScanIndexForward(true)
                .withMaxResultSize(request.getLimit());

        if(request.getLastFolloweeAlias() != null)
        {
            PrimaryKey primaryKey = new PrimaryKey("followee_alias",
                    request.getFollowerAlias(),
                    "follower_alias",request.getLastFolloweeAlias());
            spec.withExclusiveStartKey(primaryKey);
        }
        try
        {
            QueryOutcome outcome;
            users = followTable.getIndex("follower_alias-followee_alias-index").query(spec);
            iterator = users.iterator();
            while(iterator.hasNext())
            {
                item = iterator.next();
                responseFollowees.add(item.getString("followee_alias"));
            }
            outcome = users.getLastLowLevelResult();
            Map<String, AttributeValue> lastKey = outcome.getQueryResult().getLastEvaluatedKey();
            if(lastKey != null) hasMorePages = true;


        }catch (Exception e)
        {
            System.out.println("error message : " + e.getMessage());
            throw new Exception("[Internal Server Error] Not available to getFollowee");
        }
        return new Pair<List<String>,Boolean>(responseFollowees,hasMorePages);
    }

    @Override
    public Pair<List<String>, Boolean> getFollowers(FollowerRequest request) throws Exception {
        assert request.getLimit() > 0;
        assert request.getFollowingAlias() != null;

        boolean hasMorePages = false;

        List<String> responseFollowers = new ArrayList<>();
        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":followee",request.getFollowingAlias());

        Iterator<Item> iterator;
        Item item;
        ItemCollection<QueryOutcome> users;

        QuerySpec spec = new QuerySpec().withKeyConditionExpression("followee_alias = :followee")
                .withValueMap(valueMap)
                .withScanIndexForward(true)
                .withMaxResultSize(request.getLimit());

        if(request.getLastFollowerAlias() != null)
        {
            PrimaryKey primaryKey = new PrimaryKey("followee_alias",
                    request.getFollowingAlias(),
                    "follower_alias",request.getLastFollowerAlias());
            spec.withExclusiveStartKey(primaryKey);
        }

        try
        {
            QueryOutcome outcome;
            users = followTable.query(spec);
            iterator = users.iterator();
            while(iterator.hasNext())
            {
                item = iterator.next();
                responseFollowers.add(item.getString("follower_alias"));
            }
            outcome = users.getLastLowLevelResult();
            Map<String, AttributeValue> lastKey = outcome.getQueryResult().getLastEvaluatedKey();
            if(lastKey != null) hasMorePages = true;


        }catch (Exception e)
        {
            System.out.println("error message : " + e.getMessage());
            throw new Exception("[Internal Server Error] Not available to getFollowee");
        }
        return new Pair<List<String>,Boolean>(responseFollowers,hasMorePages);

    }

    //create get all the list of follower alias
    @Override
    public List<String> getFollowerAlias(User user)
    {
        List<String> followerAlias = new ArrayList<>();
        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":followee",user.getAlias());

        Iterator<Item> iterator;
        Item item;
        ItemCollection<QueryOutcome> users;

//        QuerySpec spec = new QuerySpec()
//                .withKeyConditionExpression("followee_alias = :followee")
//                .withValueMap(new ValueMap()
//                        .withString(":follower_alias", "Amazon DynamoDB#DynamoDB Thread 1"));

        QuerySpec spec = new QuerySpec().withKeyConditionExpression("followee_alias = :followee")
                .withValueMap(valueMap);

        QueryOutcome outcome;
        users = followTable.query(spec);
        iterator = users.iterator();
        while(iterator.hasNext())
        {
            item = iterator.next();
            followerAlias.add(item.getString("follower_alias"));
        }
        System.out.println(followerAlias.size());
        return followerAlias;

    }

    @Override
    public void addFollowBatch(List<String> followerAliasList) {
        TableWriteItems items = new TableWriteItems(followTable.getTableName());
        for (String followerAlias : followerAliasList)
        {
            Item item = new Item().withPrimaryKey("followee_alias","@matt")
                    .withString("follower_alias",followerAlias);
            items.addItemToPut(item);

            if (items.getItemsToPut() != null && items.getItemsToPut().size() == 25) {
                loopBatchWrite(items);
                items = new TableWriteItems(followTable.getTableName());
            }
        }
        if (items.getItemsToPut() != null && items.getItemsToPut().size() > 0) {
            loopBatchWrite(items);
        }
    }

    @Override
    public boolean isFollower(IsFollowerRequest request) throws Exception {
        assert request.getFollowee() != null;
        assert request.getFollower() != null;

        boolean isFollower = false;

        ItemCollection<QueryOutcome> items;
        Iterator<Item> iterator;
        Item item;
        QuerySpec spec;

        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":follower",request.getFollower().getAlias());

        try
        {
            spec = new QuerySpec().withKeyConditionExpression("follower_alias = :follower")
                    .withValueMap(valueMap)
                    .withScanIndexForward(true);
            items = followTable.getIndex("follower_alias-followee_alias-index").query(spec);
            iterator = items.iterator();
            while(iterator.hasNext())
            {
                item = iterator.next();
                String followee = item.getString("followee_alias");
                if(followee.equals(request.getFollowee().getAlias()))
                {
                    isFollower = true;
                    break;
                }
            }
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            throw new Exception("[Internal Server Error] Not available to check is follower");
        }
        return isFollower;

    }

    private void loopBatchWrite(TableWriteItems items) {

        // The 'dynamoDB' object is of type DynamoDB and is declared statically in this example
        BatchWriteItemOutcome outcome = dynamoDB.batchWriteItem(items);
        logger.info("Wrote Follows Batch");

        // Check the outcome for items that didn't make it onto the table
        // If any were not added to the table, try again to write the batch
        while (outcome.getUnprocessedItems().size() > 0) {
            Map<String, List<WriteRequest>> unprocessedItems = outcome.getUnprocessedItems();
            outcome = dynamoDB.batchWriteItemUnprocessed(unprocessedItems);
            logger.info("Wrote more Follows");
        }
    }




}
