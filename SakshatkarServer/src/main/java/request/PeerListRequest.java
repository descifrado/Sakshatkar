package request;

import constants.RequestCode;
import data.File;

import java.io.Serializable;

public class PeerListRequest extends Request implements Serializable
{
    private File file;
    public PeerListRequest(File file)
    {
        this.file=file;
    }
    public File getFile() throws CloneNotSupportedException
    {
        return (File) this.file.clone();
    }

    @Override
    public RequestCode getRequestCode() {
        return RequestCode.PEERLIST_REQUEST;
    }
}
