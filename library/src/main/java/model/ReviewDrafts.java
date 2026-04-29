package model;

public class ReviewDrafts {
    private int id;
    private String reader_id;
    private int slot; // 0,1,2
    private String title;
    private String isbn;
    private String pen_name;
    private String book_intro;
    private String book_excerpt;
    private String content;
    private int word_count;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getReader_id() { return reader_id; }
    public void setReader_id(String reader_id) { this.reader_id = reader_id; }

    public int getSlot() { return slot; }
    public void setSlot(int slot) { this.slot = slot; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getPen_name() { return pen_name; }
    public void setPen_name(String pen_name) { this.pen_name = pen_name; }

    public String getBook_intro() { return book_intro; }
    public void setBook_intro(String book_intro) { this.book_intro = book_intro; }

    public String getBook_excerpt() { return book_excerpt; }
    public void setBook_excerpt(String book_excerpt) { this.book_excerpt = book_excerpt; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getWord_count() { return word_count; }
    public void setWord_count(int word_count) { this.word_count = word_count; }
}
