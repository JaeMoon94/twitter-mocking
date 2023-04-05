package edu.byu.cs.tweeter.server.dao.facotryImplement;

import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.google.gson.Gson;

import edu.byu.cs.tweeter.model.net.request.FollowQueueRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.FollowQueueResponse;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.AmazonSQS;
import edu.byu.cs.tweeter.server.dao.DTO;

public class DTOImp implements DTO {
    final String ADD_STATUS_QUEUE_URL = "https://sqs.us-east-2.amazonaws.com/215837010331/PostStatusQueue";
    final String ADD_FOLLOWERS_QUEUE_URL = "https://sqs.us-east-2.amazonaws.com/215837010331/TriggerFinalLambdaQueue";
    @Override
    public boolean addPostToQueue(PostStatusRequest request) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(request);
            String messageBody = json;

            SendMessageRequest sendMessage = new SendMessageRequest()
                    .withQueueUrl(ADD_STATUS_QUEUE_URL)
                    .withMessageBody(messageBody);

            AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
            SendMessageResult result = sqs.sendMessage(sendMessage);

            System.out.println("End of Function printing result : " + result.toString());
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        return true;
    }

    @Override
    public FollowQueueResponse addFollowerToQueue(FollowQueueRequest request) {
        System.out.println("Testing AddFollower To queue");
        Gson gson = new Gson();
        String json = gson.toJson(request);
        String messageBody = json;
        SendMessageRequest sendMessage = new SendMessageRequest()
                .withQueueUrl(ADD_FOLLOWERS_QUEUE_URL)
                .withMessageBody(messageBody);

        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        SendMessageResult result = sqs.sendMessage(sendMessage);
        System.out.println("End of Function printing result : " + result.toString());

        return new FollowQueueResponse(true);
    }
}
