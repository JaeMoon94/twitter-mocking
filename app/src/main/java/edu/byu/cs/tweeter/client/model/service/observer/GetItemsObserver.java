package edu.byu.cs.tweeter.client.model.service.observer;

import java.util.List;

public interface GetItemsObserver <T> extends ServiceObserver{
    void getItemSucceed(List<T> items, boolean hasMorePages);
}
