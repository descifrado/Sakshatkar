package request;

import constants.RequestCode;

import java.io.Serializable;

public class FriendSuggestionRequest extends Request implements Serializable
{
    private String userUID;

    public FriendSuggestionRequest(String userUID)
    {
        this.userUID=userUID;
    }

    public String getUserUID() {
        return userUID;
    }

    @Override
    public RequestCode getRequestCode() {
        return RequestCode.FRIENDSUGGESTION_REQUEST;
    }
}