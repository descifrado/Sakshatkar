package data;


import constants.FileType;

import java.io.Serializable;
import java.util.Set;

/**
 *
 */
public class File implements Serializable, Cloneable {
    /**
     *
     **/
    private String fileUID;
    private String fileName;
    private FileType type;
    private Set tags;

    /**
     *
     * @param fileUID
     * @param fileName
     * @param type
     * @param tags
     */
    public File(String fileUID, String fileName, FileType type, Set tags) {
        this.fileUID = fileUID;
        this.fileName = fileName;
        this.type = type;
        this.tags = tags;
    }

    /**
     *
     * @return
     */
    public String getFileUID() {
        return fileUID;
    }

    /**
     *
     * @param fileUID
     */
    public void setFileUID(String fileUID) {
        this.fileUID = fileUID;
    }

    /**
     *
     * @return
     */
    public String getFileName() {
        return fileName;
    }

    /**
     *
     * @param fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     *
     * @return
     */
    public FileType getType() {
        return type;
    }

    /**.
     *
     * @param type
     */
    public void setType(FileType type) {
        this.type = type;
    }

    /**
     *
     * @return
     */
    public Set getTags() {
        return tags;
    }

    /**
     *
     * @param tags
     */
    public void setTags(Set tags) {
        this.tags = tags;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj==null)return false;
        File temp= (File) obj;
        return temp.getFileUID().equals(this.fileUID);
    }
}