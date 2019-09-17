package request;

import constants.RequestCode;

import java.io.Serializable;

public class FileTransferRequest extends Request implements Serializable {
    String userID;
    String userName;
    String fileName;

    public FileTransferRequest(String userID, String userName, String fileName) {
        this.userID = userID;
        this.userName = userName;
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public RequestCode getRequestCode() {
        return  RequestCode.FILETRANSFER_REQUEST;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }
}
