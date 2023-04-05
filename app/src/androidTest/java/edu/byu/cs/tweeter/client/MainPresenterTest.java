package edu.byu.cs.tweeter.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.PostStatusService;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.text.ParseException;

public class MainPresenterTest {
    private MainPresenter.View mockView;
    private PostStatusService mockPostStatusService;
    private PostStatusService.PostStatusObserver mockObserver;
    private Cache mockCache;

    String inputPost = "something here and there";
    private MainPresenter mainPresenterSpy;

    @BeforeEach
    public void setup()
    {
        mockView = Mockito.mock(MainPresenter.View.class);
        mockPostStatusService = Mockito.mock(PostStatusService.class);
//        mockCache = Mockito.mock(Cache.class);
        mainPresenterSpy = Mockito.spy(new MainPresenter(mockView));

        Mockito.doReturn(mockPostStatusService).when(mainPresenterSpy).get();
        Mockito.doReturn(mockObserver).when(mainPresenterSpy).getObserver();
    }

    @Test
    public void test_postSuccess() throws ParseException {
        Answer<Void> successAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {

                PostStatusService.PostStatusObserver observer = invocation.getArgument(1, PostStatusService.PostStatusObserver.class);
                observer.postStatusSucceed("Successfully Posted!");
                return null;
            }
        };
        Mockito.doAnswer(successAnswer).when(mockPostStatusService).postStatus(Mockito.anyString(), Mockito.any());

        mainPresenterSpy.postStatus(inputPost);
        Mockito.verify(mockView).displayInfoMessage("Posting Status...");
        Mockito.verify(mockView).displayInfoMessage("Successfully Posted!");
    }


    @Test
    public void test_postFailed() throws ParseException {
        Answer<Void> successAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {

                PostStatusService.PostStatusObserver observer = invocation.getArgument(1, PostStatusService.PostStatusObserver.class);
                observer.handleFailure("Failed to post status: error message");
                return null;
            }
        };
        Mockito.doAnswer(successAnswer).when(mockPostStatusService).postStatus(Mockito.anyString(), Mockito.any());

        mainPresenterSpy.postStatus(inputPost);

        Mockito.verify(mockView).displayInfoMessage("Posting Status...");
        Mockito.verify(mockView).displayFailedMessage("Failed to post status: error message");
        Mockito.verify(mockView, Mockito.times(0)).displayInfoMessage(inputPost);
    }

    @Test
    public void test_postFailedWithException() throws ParseException {
        Exception exception = new Exception("Failed to post status because of exception: Exception Message");
        Answer<Void> successAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {

                PostStatusService.PostStatusObserver observer = invocation.getArgument(1, PostStatusService.PostStatusObserver.class);
                observer.handleException(exception);
                return null;
            }
        };
        Mockito.doAnswer(successAnswer).when(mockPostStatusService).postStatus(Mockito.anyString(), Mockito.any());

        mainPresenterSpy.postStatus(inputPost);

        Mockito.verify(mockView).displayInfoMessage("Posting Status...");
        Mockito.verify(mockView).displayExceptionMessage(exception.getMessage());
        Mockito.verify(mockView, Mockito.times(0)).displayInfoMessage(inputPost);
    }

}
