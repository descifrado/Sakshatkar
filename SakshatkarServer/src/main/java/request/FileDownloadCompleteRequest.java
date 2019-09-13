package request;

import constants.RequestCode;
import data.File;

import java.io.Serializable;

public class FileDownloadCompleteRequest extends FileUploadRequest implements Serializable  {
    public FileDownloadCompleteRequest(File file, String userUID, String userIP) {
        super(file, userUID, userIP);
    }

    @Override
    public RequestCode getRequestCode() {
        return RequestCode.FILEDOWNLOADCOMPLETE_REQUEST;
    }
}
