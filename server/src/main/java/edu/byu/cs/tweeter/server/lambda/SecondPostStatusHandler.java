package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.gson.Gson;

import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.server.dao.factory.FactoryProvider;
import edu.byu.cs.tweeter.server.service.StatusService;


public class SecondPostStatusHandler implements RequestHandler<SQSEvent, Void> {

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        StatusService statusService = new StatusService(new FactoryProvider());
        Gson gson = new Gson();
        PostStatusRequest request = gson.fromJson(event.getRecords().get(0).getBody(), PostStatusRequest.class);
        try {
            statusService.triggerSecondQeueAndFetchFollowers(request);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Second post status Error : " + e.getMessage());
        }
        return null;
    }
}
