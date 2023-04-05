package edu.byu.cs.tweeter.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.model.service.StoryService;
import edu.byu.cs.tweeter.client.model.service.observer.GetItemsObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;

public class getStoryTest {
    private StoryService storyService;
    private GetItemsObserver observer;
    private User user;
    private CountDownLatch latch;

    @BeforeEach
    public void setUP()
    {
        storyService = Mockito.spy(new StoryService());
        user = FakeData.getInstance().getFirstUser();

        observer = new getItemsObserverMock();
        latch = new CountDownLatch(1);
    }

     private class getItemsObserverMock implements GetItemsObserver<Status>{

        boolean isSuccess;
        List<Status> items;
        boolean hasMorePages;
        String message;
        Exception ex;


        @Override
        public void getItemSucceed(List<Status> items, boolean hasMorePages) {
            this.isSuccess = true;
            this.items = items;
            this.hasMorePages = hasMorePages;
            latch.countDown();
        }

        @Override
        public void handleFailure(String message) {
            this.isSuccess = false;
            this.message = message;
            latch.countDown();
        }

        @Override
        public void handleException(Exception ex) {
             this.isSuccess = false;
             this.ex = ex;
             latch.countDown();
        }
    }
    @Test
    public void getSotryTest() throws InterruptedException {
        storyService.getStoryService(user, 10, null, observer);
        latch.await();
        Mockito.verify(observer).getItemSucceed(Mockito.anyList(), Mockito.anyBoolean());
    }

}

