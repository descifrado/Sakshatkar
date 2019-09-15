package request;

import constants.RequestCode;

import java.io.Serializable;

public class OnlineUserRequest extends Request implements Serializable
{
    private String req="Online Users ki List Bhosdike";

    public OnlineUserRequest()
    {

    }

    @Override
    public RequestCode getRequestCode() {
        return RequestCode.ONLINEUSER_REQUEST;
    }
}
