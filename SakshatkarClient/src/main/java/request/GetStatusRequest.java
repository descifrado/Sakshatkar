package request;

import constants.RequestCode;

import java.io.Serializable;

public class GetStatusRequest extends Request implements Serializable
{

    private String userUID;
    public GetStatusRequest(String userUID)
    {
        this.userUID=userUID;
    }

    public String getUserUID() {
        return userUID;
    }

    @Override
    public RequestCode getRequestCode() {
        return RequestCode.GETSTATUS_REQUEST;
    }
}
