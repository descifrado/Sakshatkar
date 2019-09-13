package tools;

import data.File;

import java.io.Serializable;
import java.util.List;

public class SharedFileResponse implements Serializable
{
    private List<File> files;
    private List<Integer> noOfDownloads;

    public List<File> getFiles()
    {
        return files;
    }

    public void setFiles(List<File> files)
    {
        this.files = files;
    }

    public List<Integer> getNoOfDownloads()
    {
        return noOfDownloads;
    }

    public void setNoOfDownloads(List<Integer> noOfDownloads)
    {
        this.noOfDownloads = noOfDownloads;
    }

    public SharedFileResponse(List<File> files, List<Integer> noOfDownloads)
    {
        this.files=files;
        this.noOfDownloads=noOfDownloads;
    }
}
