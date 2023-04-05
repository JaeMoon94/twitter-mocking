package edu.byu.cs.tweeter.model.net.request;

public class GetUserRequest {
    private String userAlis;

    GetUserRequest(){
    }

    public GetUserRequest(String userAlis){
        this.userAlis = userAlis;
    }

    public String getUserAlis() {
        return userAlis;
    }

    public void setUserAlis(String userAlis) {
        this.userAlis = userAlis;
    }
}
