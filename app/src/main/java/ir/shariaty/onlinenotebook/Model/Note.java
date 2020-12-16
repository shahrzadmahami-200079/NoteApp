package ir.shariaty.onlinenotebook.Model;

public class Note {

    private String view_text;
    private String title;
    private String description;
    private Long time;

    Note() {
    }

    public Note(String view_text, String title, String description, Long time) {
        this.view_text = view_text;
        this.title = title;
        this.description = description;
        this.time = time;
    }

    public String getView_text() {
        return view_text;
    }

    public void setView_text(String view_text) {
        this.view_text = view_text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }



}
