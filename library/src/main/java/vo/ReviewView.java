package vo;

public class ReviewView {
    private int review_id;
    private String reader_id;
    private String reader_name;
    private String book_barcode;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String publish_year;
    private String category;
    private String call_number;
    private int word_count;
    private int review_state;
    private boolean is_public;
    private String content;

    public int getReview_id() { return review_id; }
    public void setReview_id(int review_id) { this.review_id = review_id; }

    public String getReader_id() { return reader_id; }
    public void setReader_id(String reader_id) { this.reader_id = reader_id; }

    public String getReader_name() { return reader_name; }
    public void setReader_name(String reader_name) { this.reader_name = reader_name; }

    public String getBook_barcode() { return book_barcode; }
    public void setBook_barcode(String book_barcode) { this.book_barcode = book_barcode; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public String getPublish_year() { return publish_year; }
    public void setPublish_year(String publish_year) { this.publish_year = publish_year; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getCall_number() { return call_number; }
    public void setCall_number(String call_number) { this.call_number = call_number; }

    public int getWord_count() { return word_count; }
    public void setWord_count(int word_count) { this.word_count = word_count; }

    public int getReview_state() { return review_state; }
    public void setReview_state(int review_state) { this.review_state = review_state; }

    public boolean isIs_public() { return is_public; }
    public void setIs_public(boolean is_public) { this.is_public = is_public; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
