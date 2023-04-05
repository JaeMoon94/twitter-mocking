package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.gson.Gson;

import java.util.List;

import edu.byu.cs.tweeter.model.net.request.FollowQueueRequest;
import edu.byu.cs.tweeter.server.dao.factory.FactoryProvider;
import edu.byu.cs.tweeter.server.service.StatusService;

public class FinalPostStatusHandler  implements RequestHandler<SQSEvent, Void> {

    @Override
    public Void handleRequest(SQSEvent input, Context context) {
        System.out.println("Print Something from Final Post Status Request");
        StatusService service = new StatusService(new FactoryProvider());
        Gson gson = new Gson();
        List<SQSEvent.SQSMessage> eventMessage = input.getRecords();
        for (SQSEvent.SQSMessage message : eventMessage) {
            FollowQueueRequest request = gson.fromJson(message.getBody(), FollowQueueRequest.class);
            service.updateFeedWithStatuses(request);
        }
        System.out.println("Print end of code from final post status request");
        return null;
    }
}
