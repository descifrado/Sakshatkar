package request;

import constants.RequestCode;

import java.io.Serializable;

public class AddFriendRequest extends Request implements Serializable
{
    private String userUID, friendUID;
    public AddFriendRequest(String userUID, String friendUID)
    {
        this.userUID=userUID;
        this.friendUID=friendUID;
    }

    public String getUserUID() {
        return userUID;
    }

    public String getFriendUID() {
        return friendUID;
    }

    @Override
    public RequestCode getRequestCode() {
        return RequestCode.ADDFRIEND_REQUEST;
    }
}
