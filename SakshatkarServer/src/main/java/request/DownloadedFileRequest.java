package request;

import constants.RequestCode;

import java.io.Serializable;

public class DownloadedFileRequest extends Request implements Serializable
{

    private String userUID;

    public DownloadedFileRequest(String userUID)
    {
        this.userUID=userUID;
    }


    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }


    @Override
    public RequestCode getRequestCode() {
        return RequestCode.DOWNLOADEDFILES_REQUEST;
    }
}
