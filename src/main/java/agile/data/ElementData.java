package agile.data;

public class ElementData {
    
    private String tag;
    private String id;
    private String clazz;

    public String getTag() {
        return tag;
    }

    public String getId() {
        return id;
    }

    public String getClazz() {
        return clazz;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    @Override
    public String toString() {
        return "tag='" + tag + '\'' + ", id='" + id + '\'' + ", class='" + clazz + '\'';
    }
    
}
