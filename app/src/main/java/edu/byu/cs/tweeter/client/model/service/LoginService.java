package edu.byu.cs.tweeter.client.model.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.backgroundTask.handler.LoginHandler;
import edu.byu.cs.tweeter.client.model.service.observer.LoginRegisterObserver;

public class LoginService {
    public LoginService(){}
    public LoginService(String userName, String password, LoginRegisterObserver observer){
        LoginTask loginTask = new LoginTask(userName,password, new LoginHandler(observer));
        BackgroundTaskUtils.runTask(loginTask);
    }
}
