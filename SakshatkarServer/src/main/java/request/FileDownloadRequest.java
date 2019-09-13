package request;

import constants.RequestCode;
import data.File;

public class FileDownloadRequest extends Request
{
    private File file;

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    private String userUID;
    public FileDownloadRequest(File file, String userUID)
    {
        this.file=file;
        this.userUID=userUID;
    }

    public File getFile()
    {
        return file;
    }
    public void setFile(File file)
    {
        this.file=file;
    }
    @Override
    public RequestCode getRequestCode()
    {
        return RequestCode.FILEDOWNLOAD_REQUEST;
    }
}
