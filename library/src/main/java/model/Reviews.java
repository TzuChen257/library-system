package model;

public class Reviews {
    private int id;
    private String reader_id;
    private String book_barcode;
    private String content;
    private int word_count;
    private int state;
    private boolean is_public;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getReader_id() { return reader_id; }
    public void setReader_id(String reader_id) { this.reader_id = reader_id; }

    public String getBook_barcode() { return book_barcode; }
    public void setBook_barcode(String book_barcode) { this.book_barcode = book_barcode; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getWord_count() { return word_count; }
    public void setWord_count(int word_count) { this.word_count = word_count; }

    public int getState() { return state; }
    public void setState(int state) { this.state = state; }

    public boolean isIs_public() { return is_public; }
    public void setIs_public(boolean is_public) { this.is_public = is_public; }
}
