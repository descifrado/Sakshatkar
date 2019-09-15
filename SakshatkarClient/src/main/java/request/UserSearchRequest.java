package request;

import constants.RequestCode;

import java.io.Serializable;

public class UserSearchRequest extends Request implements Serializable
{

    @Override
    public RequestCode getRequestCode() {
        return null;
    }
}
