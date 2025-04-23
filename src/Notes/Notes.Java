package Notes;

public class Notes {
    private int id;
    private String title;
    private String content;

    public Notes(String title, String content) {
        this.id = 0;
        this.title = title;
        this.content = content;
    }

    public Notes(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    protected void setId(int id) {
        if (this.id == 0) {
            this.id = id;
        } else {
            throw new IllegalStateException("ID can only be set once.");
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

} 
