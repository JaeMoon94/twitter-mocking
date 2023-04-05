package edu.byu.cs.tweeter.server.dao.facotryImplement;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;
import com.amazonaws.services.dynamodbv2.xspec.M;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.dao.StatusDAO;
import edu.byu.cs.tweeter.util.Pair;

public class StatusDAOImp implements StatusDAO {

    AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withRegion("us-east-2")
            .build();

    DynamoDB dynamoDB = new DynamoDB(client);
    Table storyTable = dynamoDB.getTable("story");
    Table feedTable = dynamoDB.getTable("feed");
    private final static Logger logger = Logger.getLogger(StatusDAOImp.class.toString());


    @Override
    public Pair<List<Status>, Boolean> getFeed(FeedRequest request) throws Exception {
        assert request.getLimit() > 0;
        assert request.getUserAlias() != null;

        boolean hasMoreItems = true;
        List<Status> feedList = new ArrayList<>();
        ItemCollection<QueryOutcome> feeds = null;
        Iterator<Item> iterator;
        Item item;
        QuerySpec spec;

        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":userAlias", request.getUserAlias());
        spec = new QuerySpec().withKeyConditionExpression("user_alias = :userAlias")
                .withValueMap(valueMap).withScanIndexForward(true).withMaxResultSize(request.getLimit());

        if (request.getLastFeed() != null) {
            PrimaryKey primaryKey = new PrimaryKey("user_alias",
                    request.getLastFeed().getUser().getAlias()
                    ,"dateTime"
                    ,request.getLastFeed().getDate());
            spec.withExclusiveStartKey(primaryKey);
        }
        try {
            QueryOutcome outcome;
            feeds = feedTable.query(spec);
            iterator = feeds.iterator();

            while(iterator.hasNext())
            {
                item = iterator.next();
                feedList.add(new Status(item.getString("post"),
                        new User(item.getString("owner_firstName"),
                                item.getString("owner_lastName"),
                                item.getString("owner_Url")),
                        item.getString("dateTime"),
                        item.getList("urls"),
                        item.getList("mentions")));
            }

            outcome = feeds.getLastLowLevelResult();
            Map<String, AttributeValue> lastKey = outcome.getQueryResult().getLastEvaluatedKey();
            if(lastKey == null) hasMoreItems = false;

            }catch (Exception e)
            {
                System.out.println("Error message: " + e.getMessage());
                throw new Exception("[Internal Server Error] Not available to get Storiesssssss");
            }
            return new Pair<>(feedList, hasMoreItems);
    }

    @Override
    public Pair<List<Status>, Boolean> getStory(StoryRequest request, User user) throws Exception {
        assert request.getLimit() > 0;
        assert request.getUserAlias() != null;

        boolean hasMoreItems = true;
        List<Status> storyList = new ArrayList<>();
        ItemCollection<QueryOutcome> stories = null;
        Iterator<Item> iterator;
        Item item;
        QuerySpec spec;

        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":userAlias", request.getUserAlias());
        spec = new QuerySpec().withKeyConditionExpression("user_alias = :userAlias")
                .withValueMap(valueMap).withScanIndexForward(true).withMaxResultSize(request.getLimit());

        if (request.getLastStory() != null) {
            PrimaryKey primaryKey = new PrimaryKey("user_alias",
                    request.getLastStory().getUser().getAlias()
                    ,"dateTime"
                    ,request.getLastStory().getDate());
            spec.withExclusiveStartKey(primaryKey);
        }

        try {
            QueryOutcome outcome;
            stories = storyTable.query(spec);
            iterator = stories.iterator();

            while(iterator.hasNext())
            {
                item = iterator.next();
                storyList.add(new Status(item.getString("post"),
                        user,
                        item.getString("dateTime"),
                        item.getList("urls"),
                        item.getList("mentions")));
            }

            outcome = stories.getLastLowLevelResult();
            Map<String, AttributeValue> lastKey = outcome.getQueryResult().getLastEvaluatedKey();
            if(lastKey == null) hasMoreItems = false;

        }catch (Exception e)
        {
            System.out.println("Error message: " + e.getMessage());
            throw new Exception("[Internal Server Error] Not available to get Storiesssssss");
        }
        return new Pair<>(storyList, hasMoreItems);
    }

    @Override
    public void postStatus(PostStatusRequest request, List<String> followerAlias) throws Exception {
        try {
            storyTable.putItem(new Item()
                    .withPrimaryKey("user_alias", request.getStatus().getUser().getAlias(),
                            "dateTime", request.getStatus().getDate())
                    .withString("post", request.getStatus().getPost())
                    .withList("mentions", request.getStatus().getMentions())
                    .withList("urls", request.getStatus().getUrls()));
            for (String alias : followerAlias) {
                feedTable.putItem(new Item()
                        .withPrimaryKey("user_alias", alias,
                                "dateTime", request.getStatus().getDate())
                        .withString("post", request.getStatus().getPost())
                        .withString("owner_alias", request.getStatus().getUser().getAlias())
                        .withString("owner_firstName",request.getStatus().getUser().getFirstName())
                        .withString("owner_lastName", request.getStatus().getUser().getLastName())
                        .withString("owner_Url", request.getStatus().getUser().getImageUrl())
                        .withList("mentions", request.getStatus().getMentions())
                        .withList("urls", request.getStatus().getUrls()));
            }
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            throw new Exception("[Internal Server Error] Not available to post");
        }
    }

    @Override
    public String getOwnerAlias(FeedRequest request) {
        String alias;
        try {
            GetItemSpec spec = new GetItemSpec().withPrimaryKey("user_alias", request.getUserAlias(),"dateTime",request.getLastFeed().getDate());
            System.out.println("user_alias: " + request.getUserAlias());
            Item userAliasOutcome = feedTable.getItem(spec);
            alias = userAliasOutcome.getString("owner_alias");
            return alias;

        }catch (Exception e)
        {
            System.out.println("user_alias: " + request.getUserAlias());
            System.out.println("getOwner Alias Error " + e.getMessage());
        }

        return "error";
    }

    @Override
    public void addFeedBatch(List<String> followerAlias, Status status) {
        TableWriteItems items = new TableWriteItems(feedTable.getTableName());

        for (String el : followerAlias)
        {
            Item item  = new Item()
                    .withPrimaryKey("user_alias", el , "dateTime", status.getDate())
                    .withString("post", status.getPost())
                    .withList("mentions", status.getMentions())
                    .withList("urls", status.getUrls())
                    .withString("owner_alias", status.getUser().getAlias())
                    .withString("owner_Url",status.getUser().getImageUrl());

            items.addItemToPut(item);

            if (items.getItemsToPut() != null && items.getItemsToPut().size() == 25) {
                loopBatchWrite(items);
                items = new TableWriteItems(feedTable.getTableName());
            }
        }

        if (items.getItemsToPut() != null && items.getItemsToPut().size() > 0) {
            loopBatchWrite(items);
        }

    }

    private void loopBatchWrite(TableWriteItems items) {

        // The 'dynamoDB' object is of type DynamoDB and is declared statically in this example
        BatchWriteItemOutcome outcome = dynamoDB.batchWriteItem(items);
        logger.info("Wrote Feed Batch");

        // Check the outcome for items that didn't make it onto the table
        // If any were not added to the table, try again to write the batch
        while (outcome.getUnprocessedItems().size() > 0) {
            Map<String, List<WriteRequest>> unprocessedItems = outcome.getUnprocessedItems();
            outcome = dynamoDB.batchWriteItemUnprocessed(unprocessedItems);
            logger.info("Wrote Feed Users");
        }
    }

}
