package request;

import constants.RequestCode;

import java.io.Serializable;

public class UserSearchRequest extends Request implements Serializable
{
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private  String name;

    public UserSearchRequest(String name)
    {
        this.name=name;
    }
    @Override
    public RequestCode getRequestCode() {
        return null;
    }
}
