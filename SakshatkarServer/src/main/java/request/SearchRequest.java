package request;

import constants.FileType;
import constants.RequestCode;

import java.util.ArrayList;
import java.util.List;

/**
 *  Request class to handle search utility
 */
public class SearchRequest extends Request{

    /**
     *  Variable for search string
     */
    private String name;
    private FileType type;
    private List<String> tags;

    /**
     * Constructor for initializing search request
     * @param name Search String for search by name
     * @param type Search String for search by type
     * @param tags List of String for search by tags
     */
    public SearchRequest(String name, FileType type, List<String> tags) {
        if (tags==null)this.tags=new ArrayList<String>();
        else this.tags = tags;

        this.name = name;
        this.type = type;
    }

    /**
     *
     * @param name
     */
    public SearchRequest(String name) {
        this(name, FileType.ALL,null);
    }

    /**
     *
     * @param type
     */
    public SearchRequest(FileType type) {
        this("",type,null);
    }

    /**
     *
     * @param tags
     */
    public SearchRequest(List<String> tags) {
        this("", FileType.ALL,tags);
    }

    /**
     *
     * @param name
     * @param type
     */
    public SearchRequest(String name, FileType type) {
        this(name,type,null);
    }

    /**
     *
     * @param type
     * @param tags
     */
    public SearchRequest(FileType type, List<String> tags) {
        this("",type,tags);
    }

    /**
     *
     * @param name
     * @param tags
     */
    public SearchRequest(String name, List<String> tags) {
        this(name, FileType.ALL,tags);
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public FileType getType() {
        return type;
    }

    /**
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
    public List<String> getTags() {
        return tags;
    }

    /**
     *
     * @param tags
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     *
     * @return
     */
    @Override
    public RequestCode getRequestCode(){
        return RequestCode.SEARCH_REQUEST;
    }
}
