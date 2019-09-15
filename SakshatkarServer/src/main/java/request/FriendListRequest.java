package request;

import constants.RequestCode;

import java.io.Serializable;

public class FriendListRequest extends Request implements Serializable
{
    private String userUID;
    public FriendListRequest(String userUID)
    {
        this.userUID=userUID;
    }

    public String getUserUID() {
        return userUID;
    }

    @Override
    public RequestCode getRequestCode() {
        return RequestCode.FRIENDLIST_REQUEST;
    }
}