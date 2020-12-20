package ir.shariaty.onlinenotebook.Model;

public class Note {


    private String title;
    private String description;
    private Long time;

    Note() {
    }

    public Note( String title, String description, Long time) {

        this.title = title;
        this.description = description;
        this.time = time;
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
