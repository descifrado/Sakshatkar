package request;

import constants.RequestCode;

import java.io.Serializable;

public class UpdateUserHistoryRequest extends Request implements Serializable
{
    private String fileUID,userUID,downloaded,shared;

    public UpdateUserHistoryRequest(String userUID, String fileUID, String downloaded, String shared)
    {
        this.fileUID=fileUID;
        this.userUID=userUID;
        this.downloaded=downloaded;
        this.shared=shared;
    }

    public String getFileUID() {
        return fileUID;
    }

    public void setFileUID(String fileUID) {
        this.fileUID = fileUID;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getDownloaded()
    {
        return downloaded;
    }

    public void setDownloaded(String downloaded) {
        this.downloaded = downloaded;
    }

    public String getShared() {
        return shared;
    }

    public void setShared(String shared) {
        this.shared = shared;
    }

    @Override
    public RequestCode getRequestCode() {
        return RequestCode.UPDATEUSERHISTORY_REQUEST;
    }
}
