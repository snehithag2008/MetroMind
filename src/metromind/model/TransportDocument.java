package metromind.model;

public class TransportDocument {
    private final String id, title, type, content;
    public TransportDocument(String id, String title, String type, String content) {
        this.id = id; this.title = title; this.type = type; this.content = content;
    }
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getType() { return type; }
    public String getContent() { return content; }
    @Override public String toString() {
        return id + " | " + title + " [" + type + "]\n  " + content;
    }
}
