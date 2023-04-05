package edu.byu.cs.tweeter.client.presenter;

public interface View {
    void displayExceptionMessage(String message);
    void displayFailedMessage(String message);
    void displayInfoMessage(String message);
}
