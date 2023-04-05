package edu.byu.cs.tweeter.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import java.sql.Array;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.model.service.LoginService;
import edu.byu.cs.tweeter.client.model.service.observer.GetItemsObserver;
import edu.byu.cs.tweeter.client.model.service.observer.LoginRegisterObserver;
import edu.byu.cs.tweeter.client.presenter.LoginPresenter;
import edu.byu.cs.tweeter.client.presenter.LoginRegisterView;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class PostStatusTest {
    private User currentTestUser;
    private User logginedUser;
    private AuthToken logginedUserToken;
    private Status testStatus;
    private List<Status> itemList;

    private MainPresenter mainPresenterspy;
    private LoginService loginService;
    private MainPresenter.View view;


    @BeforeEach
    public void setUP()
    {
        view = Mockito.mock(MainPresenter.View.class);
        currentTestUser = new User("Harry","Potter","@harry","https://testingbyu.s3.us-east-2.amazonaws.com/%40harry");
        testStatus = new Status("Pass off Text",currentTestUser, LocalDateTime.now().toString(),new ArrayList<>(),new ArrayList<>());

        logginedUserToken = new AuthToken();

        itemList = new ArrayList<>();
        mainPresenterspy = Mockito.spy(new MainPresenter(view));

    }

    @Test
    public void postIntegrationTest() throws ParseException, InterruptedException {
        loginService = new LoginService(currentTestUser.getAlias(), "123", new LoginRegisterObserver() {
            @Override
            public void handleSuccess(User user, AuthToken token) {
                logginedUser = user;
                logginedUserToken = token;
            }

            @Override
            public void handleFailure(String message) {

            }

            @Override
            public void handleException(Exception exception) {

            }
        });

        Thread.sleep(4000);

        itemList.add(testStatus);
        mainPresenterspy.postStatus(testStatus.getPost());
        Thread.sleep(1000);
        Mockito.verify(mainPresenterspy).postStatus(Mockito.any());

        Thread.sleep(4000);


        mainPresenterspy.getStoryService().getStoryService(currentTestUser, 10, testStatus, new GetItemsObserver() {
            @Override
            public void getItemSucceed(List items, boolean hasMorePages) {
                Assertions.assertEquals(testStatus, items.get(0));
            }

            @Override
            public void handleFailure(String message) {
            }

            @Override
            public void handleException(Exception exception) {
            }
        });

        Thread.sleep(4000);
    }
}
