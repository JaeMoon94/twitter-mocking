package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FeedService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.GetItemsObserver;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class ItemPresenter<T> implements UserService.UserObserver, GetItemsObserver<T> {
    protected static final int PAGE_SIZE = 10;
    private static final String Log_TAG = "ItemPresenter";
    public interface ItemView <T> extends View{
        void addItems(List<T> items);
        void setLoading(boolean value);
        void navigateToUser(User user);
        void navigateToURL(String URL);
    }

    protected boolean isLoading = false;
    protected boolean hasMorePages = true;
    protected T lastItem = null;
    protected ItemView<T> view;

    /*
    Get Items Part

     */

    public void loadMoreItem(User user)
    {
        if(!isLoading && hasMorePages) {
            setLoading(true);
            view.setLoading(true);
            getService(user);
        }
    }

    public abstract void getService(User user);

    @Override
    public void getItemSucceed(List<T> items, boolean hasMorePages) {
        setLoading(false);
        view.setLoading(false);
        lastItem = (items.size() > 0) ? items.get(items.size() - 1) : null;
        this.hasMorePages = hasMorePages;
        view.addItems(items);
    }

    @Override
    public void handleFailure(String message) {
        view.displayFailedMessage( "Failed to get feed: " + message);
    }

    @Override
    public void handleException(Exception e) {
        view.displayExceptionMessage("Failed to get following because of exception: " + e.getMessage());
    }



    /*
    User Part
     */

    public void navigateToUser(String alias)
    {
        if (alias.contains("http")) {
            view.navigateToURL(alias);
        } else {
            new UserService().getUserService(alias,this);
            view.displayInfoMessage("Getting user's profile...");
        }
    }

    @Override
    public void getUserSucceed(User user) {
        view.navigateToUser(user);
    }

//    @Override
//    public void getUserFailed(String message) {
//        view.displayFailedMessage("Failed to get user's profile: " + message);
//    }
//
//    @Override
//    public void getUserException(Exception e) {
//        view.displayExceptionMessage("Failed to get user's profile because of exception: " + e.getMessage());
//    }


    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public boolean isHasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }
}
