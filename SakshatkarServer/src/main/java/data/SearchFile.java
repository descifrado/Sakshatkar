package data;

import java.io.Serializable;

public class SearchFile extends File implements Serializable,Comparable {
    private int peers;

    public SearchFile(File file, int peers) {
        super(file.getFileUID(),file.getFileName(),file.getType(),file.getTags());
        this.peers = peers;
    }

    @Override
    public String toString() {
        return super.getFileName();
    }

    @Override
    public int compareTo(Object o) {
        SearchFile temp= (SearchFile) o;
        return Integer.valueOf(temp.peers).compareTo(this.peers);
    }
}
